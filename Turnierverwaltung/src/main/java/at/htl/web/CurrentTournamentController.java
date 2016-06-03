package at.htl.web;

import at.htl.entity.Match;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
import at.htl.logic.TournamentFacade;
import at.htl.logic.TournamentSystems;

import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
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

    public Tournament getLatestTournament(){
        try {
            return tournamentFacade.findLatestTournament();
        }
        catch (EJBException ex){
            return null;
        }
    }
    public List<Match> getCurrentMatches(){
        if(getMatches().isEmpty()) {
            tournamentSystems.setMatchesForOneRound(getTournament().getTeams(), getMatches());
            matches = matchFacade.findMatchesByTournament(getTournament());
            return matches;
        }
        return matches;
    }
    public void buttonAction(ActionEvent actionEvent) {
        //TODO nächste Runde
    }

    public List<Match> getMatches() {
        if(matches==null){
            matches = new ArrayList<Match>();
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
}
