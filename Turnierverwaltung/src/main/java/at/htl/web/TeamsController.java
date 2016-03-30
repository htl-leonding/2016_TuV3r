package at.htl.web;

import at.htl.entity.Match;
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
        return getFinalMatch().getTeam1();
    }
    public Team getRunnerUp(){
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

    public Team getOpponent(Team team, List<Match> matches){
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
    public int getScore(Team team, Match match){
        return 0;
    }



}
