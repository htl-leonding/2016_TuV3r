package at.htl.web;

import at.htl.entity.Match;
import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
import at.htl.logic.TournamentFacade;
import at.htl.logic.TournamentSystems;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lokal on 03.06.2016.
 */
@Named
@SessionScoped
public class CurrentTournamentController implements Serializable {
    @Inject
    MatchFacade matchFacade;
    @Inject
    TournamentFacade tournamentFacade;
    @Inject
    TournamentSystems tournamentSystems;
    private Tournament tournament;
    private List<Match> matches;
    private List<Team> winnerList;
    private List<Team> loserList;
    private List<Match> previousMatches = new ArrayList<>();
    String redirect="http://localhost:8080/Turnierverwaltung/teams.xhtml";

    public Tournament getLatestTournament(){
        try {
            return tournamentFacade.findLatestTournament();
        }
        catch (EJBException ex){
            throw new EJBException("Letztes Turnier nicht gefunden",ex);
        }
    }
    public List<Match> getCurrentMatches(){
        if(getMatches().isEmpty()) {
            while (!((getWinnerList().size()
                    & -getWinnerList().size()) == getWinnerList().size())) {
                getTournament().getTeams().add(new Team("Fill-in",false));
            }
            tournamentSystems.setMatchesForOneRound(getWinnerList(), getMatches());
            List<Match> matches = matchFacade.findMatchesByTournament(getTournament());
        }
        return matches;
    }
    public void buttonAction(ActionEvent actionEvent) throws IOException {


        winnerList = new ArrayList<>();
        for (Match match : matches) {
            tournamentSystems.determineWinningTeam(match);
        }
        setTournament(getLatestTournament());
        previousMatches.addAll(matches);

        tournamentSystems.setMatchesForOneRound(getWinnerList(), getMatches());
        List<Match> matches = matchFacade.findMatchesByTournament(getTournament());
        setMatches(matches.subList(getPreviousMatches().size(),matches.size()));

        RequestContext requestContext = RequestContext.getCurrentInstance();
        if(getWinnerList().size()>1){
            clearData();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        }
        else{
            requestContext.execute("window.open('"+ redirect +"','_self')");
        }
    }

    private void clearData() {
        setTournament(null);
    }

    public List<Match> getMatches() {
        if(matches==null){
            matches = new ArrayList<>();
        }
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    public Tournament getTournament() {
        if(tournament==null){
            setTournament(getLatestTournament());
        }
        return tournament;
    }

    public List<Team> getWinnerList() {
        winnerList = new ArrayList<>();
        for (Team team : getTournament().getTeams()) {
            if(!team.HasLost()){
                winnerList.add(team);
            }
        }
        return winnerList;
    }

    public void setWinnerList(List<Team> winnerList) {
        this.winnerList=winnerList;
    }

    public List<Team> getLoserList() {
        loserList = new ArrayList<>();
        for (Team team : getTournament().getTeams()) {
            if(team.HasLost()){
                loserList.add(team);
            }
        }
        return loserList;
    }

    public void setLoserList(List<Team> loserList) {
        this.loserList = loserList;
    }

    public List<Match> getPreviousMatches() {
        if(previousMatches==null){
            return new ArrayList<>();
        }
        return previousMatches;
    }

    public void setPreviousMatches(List<Match> previousMatches) {
        this.previousMatches = previousMatches;
    }
}
