package at.htl.web;

import at.htl.entity.Match;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
import at.htl.logic.TournamentFacade;

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
public class RunningTournamentController implements Serializable {
    @Inject
    TournamentFacade tournamentFacade;
    @Inject
    MatchFacade matchFacade;

    private String tournamentName;
    private List<Match> matches;

    public String getTournamentName() {
        return tournamentFacade.findLatestTournament().getName();
    }
    public List<Match> getMatches(){
        return matchFacade.findMatchesByTournament(tournamentFacade.findLatestTournament());
    }


}
