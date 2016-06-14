package at.htl.web;

import at.htl.entity.Match;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
import at.htl.logic.TournamentFacade;
import at.htl.logic.TournamentSystems;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Lokal on 22.04.2016.
 */

@Named
@SessionScoped
public class MatchesController implements Serializable {
    @Inject
    TournamentFacade tournamentFacade;
    @Inject
    MatchFacade matchFacade;
    @Inject
    TournamentSystems systems;

    private String tournamentName;
    private List<Match> matches;


    public String getTournamentName() {
        return tournamentFacade.findLatestTournament().getName();
    }
    public List<Match> getMatches(){
        return matchFacade.findMatchesByTournament(tournamentFacade.findLatestTournament());
    }
}
