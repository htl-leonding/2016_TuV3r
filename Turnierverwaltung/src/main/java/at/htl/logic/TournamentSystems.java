package at.htl.logic;

import at.htl.entity.*;
import javafx.util.Pair;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Laurenz on 29.10.2015.
 * Double-KO-System
 * Schweizer-System
 * Leitersystem
 */
@Stateless
public class TournamentSystems {
    @PersistenceContext
    EntityManager em;
    final int Points_WON = 3;
    final int Points_DRAW = 1;
    final int GROUP_ROUND_COUNT = 2;
    final String[] GROUP_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z", "AA", "BB", "CC", "DD", "EE", "FF"};
    final int MAX_GROUP_SIZE = 5;

    /**
     * Erstellt die Gruppen, abhaengig von der Groeße der Teams, und gibt die Sieger
     * der Gruppenn zurueck
     *
     * @param tournament
     * @return
     */
    public List<Team> manageGroupPhase(Tournament tournament) {
        List<Team> teams = (List<Team>) tournament.getTeams();
        for (Team team : teams) {
            em.persist(team);
        }
        em.persist(tournament);
        for (Team team : teams) {
            team.setTournament(tournament);
        }
        mergeTeams(teams);
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group());
        //Ueberpruefen ob groups.size eine 2er potenz ist
        while (!((groups.size() & -groups.size()) == groups.size()) || teams.size() / groups.size() > MAX_GROUP_SIZE) {
            groups.add(new Group());
        }

        int countAssignedTeams = fillGroup(groups, teams);
        addRemainingTeamsInExistingGroups(countAssignedTeams, teams, groups);
        return getWinnerList(groups);
    }
    /*public List<Team> manageGroupPhase(final List<Team> teams){
        mergeTeams(teams);
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group());
        //Ueberpruefen ob groups.size eine 2er potenz ist
        while (!((groups.size() & -groups.size()) == groups.size()) || teams.size()/groups.size()> MAX_GROUP_SIZE){
            groups.add(new Group());
        }
        int countAssignedTeams = fillGroup(groups,teams);
        addRemainingTeamsInExistingGroups(countAssignedTeams,teams,groups);
        return getWinnerList(groups);
    }*/

    /**
     * Rekursive Methode, die eine Runde im KO-System darstellt
     *
     * @param teams
     * @return
     */
    public Team koSystemRound(List<Team> teams) {
        List<Team> newTeam = new ArrayList<Team>();
        List<Match> matches = new ArrayList<Match>();
        List<Team> loser;
        Team winner;

        setMatchesForOneRound(teams, matches);
        //eintragen von RANDOM ergebnissen
        matches = randomMatchesResult(matches);

        for (Match match : matches) {
            newTeam.add(determineWinningTeam(match));
        }
        loser = setLosingTeams(teams, newTeam);

        if (newTeam.size() > 1)
            //Falls noch mehr als ein Team verbleiben, eine neue Runde wird gestartet
            winner = koSystemRound(newTeam);
        else {
            printWinnerAndRunnerUp(newTeam, loser);
            return newTeam.get(0);
        }
        //Ausgeben der Verlierer
        printLosingTeams(loser);
        return winner;
    }

    /**
     * Den Teams, in der Gruppe, werden Matches gegeneinander zugewiesen und die zwei Gewinner zurueckgegeben
     *
     * @param group
     * @return
     */
    public List<Team> groupSystem(Group group) {
        group = em.merge(group);
        List<Team> team = group.getTeams();
        List<Match> matches;
        List<Match> existingMatches = new ArrayList<Match>();
        List<Team> winner = new ArrayList<Team>();
        for (int i = 0; i < GROUP_ROUND_COUNT; i++) {
            matches = addMatchesInGroupPhase(group);
            //RANDOM ergebnisse eintragen
            matches = randomMatchesResult(matches);
            existingMatches.addAll(matches);
            mergeMatches(existingMatches);
            mergeTeams(team);
        }
        sortTeamsByPoints(team);
        winner.add(team.get(0));
        winner.add(team.get(1));
        setLosingTeams(team, winner);
        printGroupPhaseList(team, group);
        return winner;
    }

    /**
     * Dies ist eine temporaere Methode, die random-Werte in Marches eintraegt
     *
     * @param matches
     * @return
     */
    private List<Match> randomMatchesResult(List<Match> matches) {
        Random random = new Random();
        for (Match match : matches) {
            match.setResult(random.nextInt(5) + ":" + random.nextInt(5));
        }
        return matches;
    }

    /**
     * Ermittelt den aktuellen Punktestand eines Teams waehrend eines Turnieres
     *
     * @param team
     * @return
     */
    public int determineTotalPoints(Team team) {
        Query q = em.createNamedQuery("match.findByTeamId");
        q.setParameter("id", team.getId());
        List<Match> matches = q.getResultList();
        int points = 0;
        for (Match match : matches) {
            if (match.getResultObject().getPointsFirstTeam() > match.getResultObject().getPointsSecondTeam() && match.getTeam1().getId() == team.getId()) {
                points = points + Points_WON;
            } else if (match.getResultObject().getPointsFirstTeam() == match.getResultObject().getPointsSecondTeam()) {
                points = points + Points_DRAW;
            } else if (match.getResultObject().getPointsFirstTeam() < match.getResultObject().getPointsSecondTeam() && match.getTeam2().getId() == team.getId()) {
                points = points + Points_WON;
            }
        }
        return points;
    }

    /**
     * Ermittelt das Team, dass das Match gewonnen hat und gibt es zurueck
     *
     * @param match
     * @return
     */
    public Team determineWinningTeam(Match match) {
        if (match.getResultObject().getPointsFirstTeam() > match.getResultObject().getPointsSecondTeam()) {
            return match.getTeam1();
        } else
            return match.getTeam2();
    }

    /**
     * Gibt die Sieger der uebergebenen Gruppen zurueck.
     *
     * @param groups
     * @return
     */
    private List<Team> getWinnerList(List<Group> groups) {
        List<Team> groupWinnerList = new ArrayList<Team>();
        for (Group group : groups) {
            groupWinnerList.addAll(groupSystem(group));
        }
        return groupWinnerList;
    }

    /**
     * Ermittelt den Rang, welchen ein Team im KO-System erreicht hat
     */
    public void getRankKoSystem() {
        Query q = em.createNamedQuery("team.findAll");
        List<Pair<Integer, Team>> teamsWithWins = new LinkedList<Pair<Integer, Team>>();

        for (Team t : (List<Team>) q.getResultList()) {
            teamsWithWins.add(new Pair<Integer, Team>(getWinsByTeam(t), t));
        }
        System.out.println(teamsWithWins);
    }

    /**
     * Ermittelt die Anzahl der gewonnen Spiele eines Teams im Turnier
     *
     * @param team
     * @return
     */
    public Integer getWinsByTeam(Team team) {
        int wins = 0;
        Query q = em.createNamedQuery("match.findByTeamId");
        q.setParameter("id", team.getId());
        List<Match> matches = q.getResultList();
        for (Match match : matches) {
            if (determineWinningTeam(match) == team)
                wins++;
        }
        return wins;
    }

    /**
     * Speichert Teams von einer Liste in die Datenbank
     *
     * @param teams
     */
    public void mergeTeams(List<Team> teams) {
        for (Team team : teams) {
            team = em.merge(team);
        }
    }

    /**
     * Speichert Matches von einer Liste in die Datenbank
     *
     * @param matches
     */
    public void mergeMatches(List<Match> matches) {
        for (Match match : matches) {
            match = em.merge(match);
        }
    }

    /**
     * Befuellt eine Gruppe mit Teams, und gibt dann die Anzahl der Teams in der Liste zurueck
     *
     * @param groups
     * @param teams
     * @return
     */
    public int fillGroup(List<Group> groups, List<Team> teams) {
        int countForEach = 0;
        int countAssignedTeams = 0;
        List<Team> addTeamToGroupList = new ArrayList<Team>();
        //Befuellen der Gruppen
        for (Group group : groups) {
            addTeamToGroupList.clear();
            group.setName(GROUP_NAMES[countForEach]);
            for (int j = 0; j < teams.size() / groups.size(); j++) {
                addTeamToGroupList.add(teams.get(countAssignedTeams));
                countAssignedTeams++;
            }
            group.setTeams(new ArrayList<Team>(addTeamToGroupList));
            countForEach++;
        }
        return countAssignedTeams;
    }

    /**
     * Fuegt die Teams, die uebrigbleiben, weil die Anzahl der Teams zum Beispiel ungerade ist,
     * zu den Gruppen hinzu, die bereits existieren.
     *
     * @param countAssignedTeams
     * @param teams
     * @param groups
     */
    private void addRemainingTeamsInExistingGroups(int countAssignedTeams, List<Team> teams,
                                                   List<Group> groups) {
        int addToGroupId = 0;
        for (int i = countAssignedTeams; i < teams.size(); i++) {
            if (addToGroupId == groups.size())
                addToGroupId = 0;
            groups.get(addToGroupId).getTeams().add(teams.get(i));
            addToGroupId++;
        }
    }

    /**
     * Erstellt Matches fuer eine Runde im KO-System und speichert diese dann in die DB
     *
     * @param teams
     * @param matches
     */
    public void setMatchesForOneRound(List<Team> teams, List<Match> matches) {
        for (int i = 0; i < teams.size(); i = i + 2) {
            //Es werden alle Matches in diesem Umlauf gesetzt
            //Match match = new Match(true, teams.get(i), teams.get(i + 1), new Result());
            Team t1 = em.find(Team.class, teams.get(i).getId());
            Team t2 = em.find(Team.class, teams.get(i + 1).getId());
            Match match = new Match(true, t1, t2, new Result());
            em.persist(match);
            matches.add(match);
        }
    }

    /**
     * Setzt das Ergebnis fuer ein Match
     *
     * @param match
     * @param result
     * @return
     */
    public Match setResultForOneMatch(Match match, Result result) {
        match.setResult(result);
        return match;
    }

    /**
     * Setzt fuer alle Verlierer das Feld hasLost auf true und speichert sie in die DB.
     * Eine Liste von den Verlierern wird dann zurueckgegeben
     *
     * @param teams
     * @param winners
     * @return
     */
    private List<Team> setLosingTeams(List<Team> teams, List<Team> winners) {
        List<Team> losers = new ArrayList<Team>();
        for (Team team : teams) {
            if (!winners.contains(team)) {
                team.setHasLost(true);
                losers.add(team);
                em.merge(team);
            }
        }
        return losers;
    }

    /**
     * Gibt die ersten zwei Raenge im KO-System aus
     *
     * @param newTeam
     * @param loser
     */
    private void printWinnerAndRunnerUp(List<Team> newTeam, List<Team> loser) {
        System.out.println("Sieger:\n" + newTeam.get(0).toString());
        newTeam.get(0).setRank(1);
        em.merge(newTeam.get(0));
        System.out.println("Runner-Up:\n" + loser.get(0).toString() + "\n\n");
        loser.get(0).setRank(2);
        em.merge(loser.get(0));
    }

    /**
     * Gibt die Verlierer der aktuellen Runde aus
     *
     * @param loser
     */
    private void printLosingTeams(List<Team> loser) {
        System.out.println("Im 1/" + loser.size() + " Finale ausgeschieden:");
        for (Team team : loser) {
            System.out.println(team.toString());
            //Endplazierung einspeichern
            team.setRank(loser.size() * 2);
        }
        System.out.println("\n");
    }

    /**
     * Gibt alle Gruppen aus, inklusive den Teams und den zugehoerigen Punkten
     *
     * @param team
     * @param group
     */
    private void printGroupPhaseList(List<Team> team, Group group) {
        System.out.println(group.toString() + " Ergebnisse:");
        for (Team team1 : team) {
            System.out.println(team1.toString() + ", Punkte: " + determineTotalPoints(team1));
        }
        System.out.println("\n");
    }

    /**
     * Fuegt fuer jedes Team in der Gruppe Matches so hinzu, dass jedes Team einmal
     * gegeneinander spielt und gibt die Matches dann zurueck
     *
     * @param group
     * @return
     */
    public List<Match> addMatchesInGroupPhase(Group group) {
        List<Team> team = group.getTeams();
        List<Match> matches = new ArrayList<Match>();
        boolean hasPlayed = false;
        for (Team team1 : team) {
            //Setzen der Gruppe
            team1.setGroup(group);
            for (Team team2 : team) {
                //Setzen der Gruppe
                team2.setGroup(group);
                //um zu Ueberpruefen, ob das Team schon spielt werden matches erstellt
                Match match = new Match(true, team1, team2, new Result());

                if (team1 != team2) {
                    for (Match match1 : matches) {
                        if (match.matchAlreadySet(match1)) {
                            hasPlayed = true;
                        }
                    }
                    if (!hasPlayed) {
                        //Eintragen in die DB
                        em.persist(match);
                        //Hinzufuegen zur Liste
                        matches.add(match);
                    }
                    hasPlayed = false;
                }
            }
        }
        return matches;
    }

    /**
     * Sortiert die Teams nach den Punkten (vom Groeßten zum Kleinsten)
     *
     * @param team
     */
    private void sortTeamsByPoints(List<Team> team) {
        Collections.sort(team, new Comparator<Team>() {
            public int compare(Team o1, Team o2) {
                if (determineTotalPoints(o1) < determineTotalPoints(o2)) {
                    return 1;
                } else if (determineTotalPoints(o1) == determineTotalPoints(o2)) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * Weist den Teams zufällige Ränge zu, wenn die Teams noch keine hat
     *
     * @param teams
     */
    private List<Team> sortTeamsRandomIfNecessary(List<Team> teams) {
        boolean hasRanks = true;
        for (Team team : teams) {
            if (team.getRank() == 0) {
                hasRanks = false;
            }
        }

        if (hasRanks && teams.size() > 0) {
            Random random = new Random();
            int max = teams.size();
            int min = 1;
            List<Integer> randoms = new ArrayList<Integer>();

            for (int i = 0; i < teams.size(); i++) {
                int newInt = random.nextInt((max - min) + 1) + min;
                while (randoms.contains(newInt)) {
                    newInt = random.nextInt((max - min) + 1) + min;
                }
                randoms.add(newInt);
            }

            for (int j = 0; j < teams.size(); j++) {
                teams.get(j).setRank(randoms.get(j));
            }
        }
        return teams;
    }

    /**
     * @return zufälliges Ergebnis
     */
    private Result randomResult() {
        Random random = new Random();
        return new Result(random.nextInt(5), random.nextInt(5));
    }

    /**
     * Überprüft, ob der Herausforderer überhaupt den Herausgeforderten herausfordern darf
     * Es dürfen Gegner mit maximal 2 Rängen Unterschied gegeneinander spielen
     *
     * @param challenger
     * @param decidedOpponent
     * @param teams
     * @return die bereits veränderte und sortierte Liste der Teams mit den neuen Rängen
     */
    public List<Team> getWinnerLeiterSystem(Team challenger, Team decidedOpponent, List<Team> teams, Result result) {
        teams = sortTeamsRandomIfNecessary(teams);
        int challengerRank = challenger.getRank();
        int opponentRank = decidedOpponent.getRank();

        if (challengerRank - opponentRank < 2) {
            if (result == null) {
                result = randomResult();
            }
            Match match = setResultForOneMatch(new Match(true, challenger, decidedOpponent, result), result);
            sortTeamsByPoints(teams);
            return teams;
        } else {
            return teams;
        }
    }
}
