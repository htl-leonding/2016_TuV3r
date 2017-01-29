package at.htl.logic;

import at.htl.entity.Team;
import at.htl.entity.Tournament;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Created by Laurenz on 16.11.2015.
 */
@Stateless
public class TeamFacade {
    @PersistenceContext
    EntityManager em;
    /*public void update(Team team) {
    }*/

    public Team save(Team team) {
        return em.merge(team);
    }

    public List<Team> findAll() {
        return em.createNamedQuery("team.findAll",Team.class)
                .getResultList();
    }

    public Team findById(long id) {
        return em.find(Team.class,id);
    }

    public List<Team> findTeamsFromLatestTournament() {
        return em.createNamedQuery("tournament.findAllTournaments",Tournament.class)
                .getResultList().get(0).getTeams();
    }
    public void remove(Team t){
        em.remove(t);
    }
}
