package at.htl.logic;

import at.htl.entity.Team;
import at.htl.entity.Tournament;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurenz on 16.11.2015.
 */
@Startup
@Singleton
public class InitBean {
    @PersistenceContext
    EntityManager em;

    @Inject
    private TournamentSystems systems;

    final int TEAM_COUNT = 17;

    @PostConstruct
    public void init(){
        List<Team> teams = new ArrayList<Team>();
        for (long j = 1; j<3;j++) {
            teams = new ArrayList<Team>();
            for (long i = 1; i < TEAM_COUNT + 1; i++) {
                Team team = new Team("Team" + i, false, 0);
                em.persist(team);
                //team = teamFacade.save(team);
                teams.add(team);
            }
            Tournament tournament = new Tournament("Schulcup"+j, LocalDate.now().minusDays(j), true, teams);
            em.persist(tournament);

            systems.koSystemRound(systems.manageGroupPhase(tournament),tournament);
            //systems.koSystemRound(teams);
            //systems.koSystemRound(systems.manageGroupPhase(teams));

            //systems.getRankKoSystem();
            tournament.setTeams(teams);
            for(Team t : teams){
                t.setTournament(tournament);
                em.merge(t);
            }
        }

    }
}
