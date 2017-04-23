package at.htl.logic;

import at.htl.entity.Round;
import at.htl.entity.Tournament;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Laurenz on 16.11.2015.
 */
@Stateless
public class RoundFacade {
    @PersistenceContext
    EntityManager em;
    /*public void update(Team team) {
    }*/

    public Round save(long id, Round r){
        r.setId(id);
        return em.merge(r);
    }

    public List<Round> findAll() {
        return em.createQuery("select r from Round r")
                .getResultList();
    }

    public Round findById(long id) {
        return em.find(Round.class,id);
    }

    public void remove(Round r){
        em.remove(r);
    }

    public void merge(Round r) {
        em.merge(r);
    }

    public List<Round> findByTournamentId(long tournamentId) {
        return em.find(Tournament.class,tournamentId).getRounds();
    }
}
