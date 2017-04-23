package at.htl.logic;

import at.htl.entity.*;
import at.htl.entity.dto.PostMatchDto;
import at.htl.entity.dto.PutMatchDto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    public Match saveByDto(PostMatchDto matchDto) {
        Match m = new Match(true,em.find(Team.class,matchDto.getTeam1Id())
                ,em.find(Team.class,matchDto.getTeam2Id())
                ,matchDto.getResult());
        m.setCourt(matchDto.getCourt());
        m.setTournament(m.getTeam1().getTournament());
        m=em.merge(m);
        try {
            m.setRound(em.find(Round.class, matchDto.getRoundId()));
            em.merge(m);
        }   catch (Exception e){
            System.out.println(e.getMessage());
        }
        return m;
    }

    public void update(long id, PutMatchDto m) {
        Match match = findById(id);
        if(m.getResult()==null){
            m.setResult(match.getResultObject());
        }
        match.setResultObject(m.getResult());
        match.setResult(m.getResult().toString());
        match.setActive(m.isActive());
        match.setStartTime(LocalTime.parse(m.getStartTime(),
                DateTimeFormatter.ofPattern("HH:mm")));
        em.merge(match);
    }

    public List<Match> findMatchesByRoundId(long roundId) {
        Round r = em.find(Round.class,roundId);
        return r.getMatches();
    }
}
