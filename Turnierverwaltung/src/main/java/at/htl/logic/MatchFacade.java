package at.htl.logic;

import at.htl.entity.Match;
import at.htl.entity.Team;
import at.htl.entity.Tournament;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Laurenz on 19.11.2015.
 */
@Stateless
public class MatchFacade {
    @PersistenceContext
    EntityManager em;

    public List<Match> findAll() {
        return em.createNamedQuery("match.findAll",Match.class)
                .getResultList();
    }

    public List<Match> findMatchesByTeam(Team team) {
        return em.createNamedQuery("match.findByTeamId",Match.class)
                .setParameter("id",team.getId())
                .getResultList();
    }

    public Match findById(long id) {
        return em.find(Match.class,id);
    }

    public List<Match> findMatchesByTournament(Tournament tournament){
        return em.createNamedQuery("match.findByTournamentId",Match.class)
                .setParameter("id",tournament.getId())
                .getResultList();
    }
    public List<Match> findMatchesByTournamentId(long tournamentId){
        return em.createQuery("select m from Match m where m.tournament.id=:tournamentId")
                .setParameter("tournamentId",tournamentId)
                .getResultList();
    }

    public Match save(long id, Match m) {
        m.setId(id);
        return em.merge(m);
    }
}
