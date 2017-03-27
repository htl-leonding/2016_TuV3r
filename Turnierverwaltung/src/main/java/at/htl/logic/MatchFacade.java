package at.htl.logic;

import at.htl.entity.Match;
import at.htl.entity.Result;
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

    public List<Match> findMatchesByTeamId(long teamId) {
        return em.createNamedQuery("match.findByTeamId",Match.class)
                .setParameter("id",teamId)
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

    public void saveByIds(Result result, long team1Id, long team2Id) {
        Match m = new Match(true,em.find(Team.class,team1Id),em.find(Team.class,team2Id),result);
        m.setTournament(m.getTeam1().getTournament());
        m.setResult(m.getResult());
        em.persist(m);
    }
}
