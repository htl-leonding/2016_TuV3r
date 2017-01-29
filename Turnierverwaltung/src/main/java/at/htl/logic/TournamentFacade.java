package at.htl.logic;

import at.htl.entity.Match;
import at.htl.entity.Team;
import at.htl.entity.Tournament;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Lokal on 12.01.2016.
 */
@Stateless
public class TournamentFacade {
    @PersistenceContext
    EntityManager em;

    public Tournament findById(long id) {
        return (Tournament) em.createQuery("Select t from Tournament t where t.id=:id")
                .setParameter("id",id)
                .getResultList().get(0);
    }

    public Tournament findLatestTournament() {
        return em.createNamedQuery("tournament.findAllTournaments",Tournament.class)
                .getResultList().
                        get(em.createNamedQuery("tournament.findAllTournaments",Tournament.class)
                        .getResultList().size()-1);
    }
    public List<Tournament> findAllTournaments() {
        return em.createNamedQuery("tournament.findAllTournaments",Tournament.class)
                .getResultList();
    }
    public List<Tournament> findAllClosedTournaments() {
        List<Tournament> tournaments= em.createNamedQuery("tournament.findAllTournaments",Tournament.class)
                .getResultList();
        if(tournaments.get(0).getActive())
            tournaments.remove(0);
        return tournaments;
    }
    public List<Team> getTeamsOrderedByRank(Tournament tournament){
        List<Team> teams = new ArrayList<Team>();
        teams = tournament.getTeams();
        Collections.sort(teams, new Comparator<Team>() {
            public int compare(Team o1, Team o2) {
                if(o1.getRank()<o2.getRank())
                    return -1;
                else if(o1.getRank()>o2.getRank())
                    return 1;
                return 0;
            }
        });
        return teams;
    }
    public Tournament save(Tournament t){
        return em.merge(t);
    }
}
