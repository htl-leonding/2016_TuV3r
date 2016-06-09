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
    int pointsWon = 3;
    int pointsDraw = 1;
    final int GROUP_ROUND_COUNT = 2;
    final String[] GROUP_NAMES = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S",
            "T","U","V","W","X","Y","Z","AA","BB","CC","DD","EE","FF"};
    int groupSize = 5;
    Tournament tournament;


    public void launchTournament(int groupSize, int pointsDraw, int pointsWon,List<String> selectedTypes,Tournament tournament){
        this.groupSize=groupSize;
        this.pointsWon=pointsWon;
        this.pointsDraw=pointsDraw;


        if(selectedTypes!=null && selectedTypes.contains("Gruppenphase")){
            koSystemRound(manageGroupPhase(tournament),null);
        }
        else{
            koSystemRound(null,tournament);
        }
    }

    /**
     * Erstellt die Gruppen, abhaengig von der Groeße der Teams, und gibt die Sieger
     * der Gruppenn zurueck
     * @param tournament
     * @return
     */
    public List<Team> manageGroupPhase(Tournament tournament) {
        setTournament(tournament);
        List<Team> teams = (List<Team>) tournament.getTeams();
        persistTeams(teams);
        persistTournament(tournament);
        for (Team team : teams) {
            team.setTournament(tournament);
        }
        mergeTeams(teams);
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group());
        //Ueberpruefen ob groups.size eine 2er potenz ist
        while (!((groups.size() & -groups.size()) == groups.size()) || teams.size() / groups.size() > groupSize) {
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
        while (!((groups.size() & -groups.size()) == groups.size()) || teams.size()/groups.size()> groupSize){
            groups.add(new Group());
        }
        int countAssignedTeams = fillGroup(groups,teams);
        addRemainingTeamsInExistingGroups(countAssignedTeams,teams,groups);
        return getWinnerList(groups);
    }*/

    /**
     * Rekursive Methode, die eine Runde im KO-System darstellt
     * @param teams
     * @return
     */
    public Team koSystemRound(List<Team> teams, Tournament tournament){
        if(tournament!=null){
            em.persist(tournament);
            teams=tournament.getTeams();
            for (Team team : teams) {
                team.setTournament(tournament);
                em.persist(team);
            }
            setTournament(tournament);
        }
        while (!((teams.size() & -teams.size()) == teams.size())) {
            teams.add(new Team("Fill-in",false));
        }
        List<Team> newTeam = new ArrayList<Team>();
        List<Match> matches = new ArrayList<Match>();
        List<Team> loser;
        Team winner;

        setMatchesForOneRound(teams,matches);
        //eintragen von RANDOM ergebnissen
        matches = randomMatchesResult(matches);

        for (Match match : matches) {
            newTeam.add(determineWinningTeam(match));
        }
        loser=setLosingTeams(teams,newTeam);

        if(newTeam.size()>1)
            //Falls noch mehr als ein Team verbleiben, eine neue Runde wird gestartet
            winner = koSystemRound(newTeam,null);
        else {
            printWinnerAndRunnerUp(newTeam,loser);
            return newTeam.get(0);
        }
        //Ausgeben der Verlierer
        printLosingTeams(loser);
        return winner;
    }

    /**
     * Den Teams, in der Gruppe, werden Matches gegeneinander zugewiesen und die zwei Gewinner zurueckgegeben
     * @param group
     * @return
     */
    public List<Team> groupSystem(Group group){
        group = em.merge(group);
        List<Team> team = group.getTeams();
        List<Match> matches;
        List<Match> existingMatches = new ArrayList<Match>();
        List<Team> winner = new ArrayList<Team>();
        for(int i = 0; i<GROUP_ROUND_COUNT; i++) {
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
        setLosingTeams(team,winner);
        printGroupPhaseList(team,group);
        return winner;
    }

    /**
     * Dies ist eine temporaere Methode, die random-Werte in Marches eintraegt
     * @param matches
     * @return
     */
    private List<Match> randomMatchesResult(List<Match> matches){
        Random random = new Random();
        for (Match match : matches) {
            int rd1=random.nextInt(5);
            int rd2=random.nextInt(5);

            if(rd1==rd2){
                rd1++;
            }
            match.setResult(rd1+":"+rd2);
        }
        return matches;
    }

    /**
     * Ermittelt den aktuellen Punktestand eines Teams waehrend eines Turnieres
     * @param team
     * @return
     */
    public int determineTotalPoints(Team team){
        Query q = em.createNamedQuery("match.findByTeamId");
        q.setParameter("id", team.getId());
        List<Match> matches = q.getResultList();
        int points=0;
        for (Match match : matches) {
            if(match.getResultObject().getPointsFirstTeam() > match.getResultObject().getPointsSecondTeam() && match.getTeam1().getId()==team.getId()){
                points=points+pointsWon;
            }
            else if(match.getResultObject().getPointsFirstTeam() == match.getResultObject().getPointsSecondTeam()){
                points=points+pointsDraw;
            }
            else if (match.getResultObject().getPointsFirstTeam() < match.getResultObject().getPointsSecondTeam() && match.getTeam2().getId()==team.getId())
            {
                points=points+pointsWon;
            }
        }
        return points;
    }

    /**
     * Ermittelt das Team, dass das Match gewonnen hat und gibt es zurueck
     * @param match
     * @return
     */
    public Team determineWinningTeam(Match match){
        if(match.getTeam1()==null){
            return match.getTeam2();
        }
        if(match.getTeam2()==null){
            return match.getTeam1();
        }
        if(match.getResultObject().getPointsFirstTeam() > match.getResultObject().getPointsSecondTeam()){
            return match.getTeam1();
        }
        else
            return match.getTeam2();
    }

    /**
     * Gibt die Sieger der uebergebenen Gruppen zurueck.
     * @param groups
     * @return
     */
    private List<Team> getWinnerList(List<Group> groups){
        List<Team> groupWinnerList = new ArrayList<Team>();
        for (Group group : groups) {
            groupWinnerList.addAll(groupSystem(group));
        }
        return groupWinnerList;
    }

    /**
     * Ermittelt den Rang, welchen ein Team im KO-System erreicht hat
     */
    public void getRankKoSystem(){
        Query q= em.createNamedQuery("team.findAll");
        List<Pair<Integer, Team>> teamsWithWins = new LinkedList<Pair<Integer, Team>>();

        for (Team t : (List<Team>)q.getResultList()){
            teamsWithWins.add(new Pair<Integer, Team>(getWinsByTeam(t), t));
        }
        System.out.println(teamsWithWins);
    }

    /**
     * Ermittelt die Anzahl der gewonnen Spiele eines Teams im Turnier
     * @param team
     * @return
     */
    public Integer getWinsByTeam(Team team) {
        int wins=0;
        Query q = em.createNamedQuery("match.findByTeamId");
        q.setParameter("id", team.getId());
        List<Match> matches = q.getResultList();
        for (Match match : matches) {
            if(determineWinningTeam(match)==team)
                wins++;
        }
        return wins;
    }

    /**
     * Speichert Teams von einer Liste in die Datenbank
     * @param teams
     */
    public void mergeTeams(List<Team> teams){
        for (Team team : teams) {
            team = em.merge(team);
        }
    }
    public void persistTeams(List<Team> teams){
        for (Team team : teams) {
            em.persist(team);
        }
    }

    public void mergeTournament(Tournament tournament){
        tournament = em.merge(tournament);
    }

    public void persistTournament(Tournament tournament){
        em.persist(tournament);
    }


    /**
     *Speichert Matches von einer Liste in die Datenbank
     * @param matches
     */
    public void mergeMatches(List<Match> matches){
        for (Match match : matches) {
            match = em.merge(match);
        }
    }

    /**
     * Befuellt eine Gruppe mit Teams, und gibt dann die Anzahl der Teams in der Liste zurueck
     * @param groups
     * @param teams
     * @return
     */
    public int fillGroup(List<Group> groups, List<Team> teams){
        int countForEach = 0;
        int countAssignedTeams =0;
        List<Team> addTeamToGroupList = new ArrayList<Team>();
        //Befuellen der Gruppen
        for (Group group : groups) {
            addTeamToGroupList.clear();
            group.setName(GROUP_NAMES[countForEach]);
            for(int j = 0; j<teams.size() / groups.size();j++){
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
     * @param countAssignedTeams
     * @param teams
     * @param groups
     */
    private void addRemainingTeamsInExistingGroups(int countAssignedTeams,List<Team> teams,
                                                   List<Group> groups){
        int addToGroupId=0;
        for (int i=countAssignedTeams; i < teams.size(); i++) {
            if(addToGroupId==groups.size())
                addToGroupId=0;
            groups.get(addToGroupId).getTeams().add(teams.get(i));
            addToGroupId++;
        }
    }

    /**
     * Erstellt Matches fuer eine Runde im KO-System und speichert diese dann in die DB
     * @param teams
     * @param matches
     */
    public void setMatchesForOneRound(List<Team> teams, List<Match> matches){
        for (int i = 0; i < teams.size()/2; i++) {
            //Es werden alle Matches in diesem Umlauf gesetzt
            //Match match = new Match(true, teams.get(i), teams.get(i + 1), new Result());
            Team t1 = em.find(Team.class, teams.get(i).getId());
            Team t2 = em.find(Team.class, teams.get(teams.size()-1-i).getId());
            Match match = new Match(true, t1, t2, new Result());
            match.setTournament(t1.getTournament());
            em.persist(match);
            matches.add(match);
        }
    }

    /**
     * Setzt das Ergebnis fuer ein Match
     * @param match
     * @param result
     * @return
     */
    public Match setResultForOneMatch(Match match, Result result){
        match.setResult(result);
        return match;
    }

    /**
     * Setzt fuer alle Verlierer das Feld hasLost auf true und speichert sie in die DB.
     * Eine Liste von den Verlierern wird dann zurueckgegeben
     * @param teams
     * @param winners
     * @return
     */
    private List<Team> setLosingTeams(List<Team> teams, List<Team> winners){
        List<Team> losers = new ArrayList<Team>();
        for (Team team : teams) {
            if(!winners.contains(team))
            {
                team.setHasLost(true);
                losers.add(team);
                em.merge(team);
            }
        }
        return losers;
    }

    /**
     * Gibt die ersten zwei Raenge im KO-System aus
     * @param newTeam
     * @param loser
     */
    private void printWinnerAndRunnerUp(List<Team> newTeam, List<Team> loser){
        System.out.println("Sieger:\n"+newTeam.get(0).toString());
        newTeam.get(0).setRank(1);
        em.merge(newTeam.get(0));
        System.out.println("Runner-Up:\n"+loser.get(0).toString()+"\n\n");
        loser.get(0).setRank(2);
        em.merge(loser.get(0));
    }

    /**
     * Gibt die Verlierer der aktuellen Runde aus
     * @param loser
     */
    private void printLosingTeams(List<Team> loser){
        System.out.println("Im 1/"+loser.size()+" Finale ausgeschieden:");
        for (Team team : loser) {
            System.out.println(team.toString());
            //Endplazierung einspeichern
            team.setRank(loser.size()*2);
        }
        System.out.println("\n");
    }

    /**
     * Gibt alle Gruppen aus, inklusive den Teams und den zugehoerigen Punkten
     * @param team
     * @param group
     */
    private void printGroupPhaseList(List<Team> team,Group group) {
        System.out.println(group.toString()+" Ergebnisse:");
        for (Team team1 : team) {
            System.out.println(team1.toString()+ ", Punkte: "+ determineTotalPoints(team1));
        }
        System.out.println("\n");
    }

    /**
     * Fuegt fuer jedes Team in der Gruppe Matches so hinzu, dass jedes Team einmal
     * gegeneinander spielt und gibt die Matches dann zurueck
     * @param group
     * @return
     */
    public List<Match> addMatchesInGroupPhase(Group group){
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
                match.setTournament(getTournament());

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
     * @param team
     */
    private void sortTeamsByPoints(List<Team> team) {
        Collections.sort(team, new Comparator<Team>() {
            public int compare(Team o1, Team o2) {
                if(determineTotalPoints(o1) <determineTotalPoints(o2)){
                    return 1;
                }
                else if(determineTotalPoints(o1) == determineTotalPoints(o2)){
                    return 0;
                }
                else
                {
                    return -1;
                }
            }
        });
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void setTournamentValues(int groupSize, int pointsDraw, int pointsWon, List<String> selectedTypes, Tournament tournament) {
        this.groupSize=groupSize;
        this.pointsWon=pointsWon;
        this.pointsDraw=pointsDraw;
    }

    /**
     * Generiert ein zufälliges Result.
     * @return
     */
    public Result generateResult(){
        Random r = new Random(2);
        return new Result(r.nextInt(2), r.nextInt(2));
    }

    /**
     * Iterative Methode, die ein Schweizer System darstellt.
     * @param teams
     * @return
     */
    public List<Team> schweizerSystem(List<Team> teams){
        sortTeamsByPoints(teams);
        List<Match> matches = new ArrayList<Match>();


        for (int i = 0; i < teams.size(); i++) {
            for (Team team1 : teams) {
                if (!team1.isOccupied()){
                    for (Team team2 : teams) {
                        if (!team2.isOccupied() && !team1.getName().equals(team2.getName())){
                            for (Match match : matches) {
                                if ((!match.getTeam1().getName().equals(team1.getName())
                                        && !match.getTeam2().getName().equals(team2.getName()))
                                        || (!match.getTeam1().getName().equals(team2.getName())
                                        && !match.getTeam2().getName().equals(team1.getName()))){
                                    matches.add(new Match(false, team1, team2, generateResult()));
                                }
                            }
                        }
                    }
                }
            }
            sortTeamsByPoints(teams);
        }
        return teams;
    }

    public Team doublekoSystem(List<Team> winnerBracket) {
        List<Team> loserBracket = new ArrayList<Team>();
        List<Match> matches;
        List<Match> matchesLoserBracket;
        List<Team> finalmatch=new ArrayList<Team>();
        List<Team> loser=new ArrayList<Team>();

        //winnerbracket bis viertel finale
        while (winnerBracket.size()>2) {

            matches = new ArrayList<Match>();
            setMatchesForOneRound(winnerBracket, matches);
            matches = randomMatchesResult(matches);

            for (Match match : matches) {
                if (winnerBracket.size()>2) {
                    if (match.getTeam1() != determineWinningTeam(match)) {
                        loserBracket.add(match.getTeam1());
                        winnerBracket.remove(match.getTeam1());
                    } else {
                        loserBracket.add(match.getTeam2());
                        winnerBracket.remove(match.getTeam2());
                    }
                }
            }

        }

        //loserbracket bis zum vietel final
        while (loserBracket.size()>2) {

            matchesLoserBracket = new ArrayList<Match>();
            setMatchesForOneRound(loserBracket, matchesLoserBracket);
            matchesLoserBracket = randomMatchesResult(matchesLoserBracket);

            for (Match match : matchesLoserBracket) {
                if(loserBracket.size()>2) {
                    if (match.getTeam1() != determineWinningTeam(match)) {
                        loserBracket.remove(match.getTeam1());
                    } else {
                        loserBracket.remove(match.getTeam2());
                    }
                }
            }
        }

        //halbfinale winnerbracket
        matches=new ArrayList<Match>();
        setMatchesForOneRound(winnerBracket, matches);
        matches = randomMatchesResult(matches);

        for(Match match:matches) {
            finalmatch.add(determineWinningTeam(match));
        }

        //halbfinale loserbracket
        matchesLoserBracket=new ArrayList<Match>();
        setMatchesForOneRound(winnerBracket, matchesLoserBracket);
        matchesLoserBracket = randomMatchesResult(matchesLoserBracket);

        for(Match match:matchesLoserBracket) {
            finalmatch.add(determineWinningTeam(match));
        }

        //finale
        matches=new ArrayList<Match>();
        setMatchesForOneRound(finalmatch, matches);
        matches = randomMatchesResult(matches);

        return determineWinningTeam(matches.get(0));
    }
}
