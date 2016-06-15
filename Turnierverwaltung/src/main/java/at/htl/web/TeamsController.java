package at.htl.web;

import at.htl.entity.Match;
import at.htl.entity.Result;
import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
import at.htl.logic.TeamFacade;
import at.htl.logic.TournamentFacade;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Lokal on 21.01.2016.
 */
@Named
@RequestScoped
public class TeamsController implements Serializable {
    @Inject
    TeamFacade teamFacade;
    @Inject
    TournamentFacade tournamentFacade;
    @Inject
    MatchFacade matchFacade;

    public List<Team> getTeams(){
        return teamFacade.findTeamsFromLatestTournament();
    }
    public List<Team> getTeamsByRank(){
        return tournamentFacade.getTeamsOrderedByRank(tournamentFacade
                        .findLatestTournament());
    }

    public Team getWinner(){
        if(getFinalMatch().getResultObject().getPointsFirstTeam()>
                getFinalMatch().getResultObject().getPointsSecondTeam())
            return getFinalMatch().getTeam1();
        else
            return getFinalMatch().getTeam2();
    }
    public Team getRunnerUp(){
        if(getFinalMatch().getResultObject().getPointsFirstTeam()<
                getFinalMatch().getResultObject().getPointsSecondTeam())
            return getFinalMatch().getTeam1();
        else
            return getFinalMatch().getTeam2();
    }
    private Match getFinal(){
        List<Match> matches= matchFacade.findMatchesByTeam(getTeamsByRank().get(0));
        return matches.get(matches.size());
    }
    public List<Team> getQuarterFinalists(){
        return getTeamsByRank().subList(0,7);
    }
    public List<Team> getSemiFinalists(){
        return getTeamsByRank().subList(0,3);
    }
    public List<Match> getQuarterFinalMatches(){
        return matchFacade
                .findAll()
                .subList(matchFacade.findAll().size()-7
                        ,matchFacade.findAll().size()-3);
    }
    public List<Match> getSemiFinalMatches(){
        return matchFacade
                .findAll()
                .subList(matchFacade.findAll().size()-3
                        ,matchFacade.findAll().size()-1);
    }
    public Match getFinalMatch(){
        return matchFacade
                .findAll()
                .get(matchFacade.findAll().size()-1);
    }

    public List<Match> getRecentMatches(Team team){
        return matchFacade.findMatchesByTeam(team);
    }

    /***
     * Gibt den Gegner des Teams in der jeweiligen Runde zurück
     * @param team
     * @param matches
     * @return
     */
    public Team getOpponent(Team team, List<Match> matches){
        if(team==null){
            return new Team(";_;",true);
        }
        for (Match match : matches) {
            if(match.getTeam1().getName().equals(team.getName())){
                return match.getTeam2();
            }
            if(match.getTeam2().getName().equals(team.getName())){
                return match.getTeam1();
            }
        }
        return null;
    }

    /***
     * Gibt die Anzahl der Punkte von einem Team in einem bestimmten Match zurück
     * @param team
     * @param match
     * @return
     */
    public int getScore(Team team, Match match){
        if(match.getResultObject().getPointsFirstTeam()==100 || team==null){
            return -1;
        }
        if(match.getTeam1().getName().equals(team.getName()))
            return match.getResultObject().getPointsFirstTeam();
        else if(match.getTeam2().getName().equals(team.getName()))
            return match.getResultObject().getPointsSecondTeam();
        return -1;
    }

    /***
     * Sucht das Match des Teams in einer bestimmten Runde
     * @param team
     * @param matches
     * @return
     */
    public Match findMatch(Team team,List<Match> matches){

        for (Match match : matches) {
            if(team==null){
                return new Match(true,null,null,new Result(100,100));
            }
            if(match.getTeam1().getName().equals(team.getName())||match.getTeam2().getName().equals(team.getName())){
                return match;
            }
        }
        Match match = new Match();
        match.setResult(new Result(100,2));
        return match;
    }
}
