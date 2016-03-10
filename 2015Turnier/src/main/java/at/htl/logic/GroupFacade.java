package at.htl.logic;

import at.htl.entity.Group;
import at.htl.entity.Team;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Laurenz on 19.11.2015.
 */
@Stateless
public class GroupFacade {
    @PersistenceContext
    EntityManager em;

    public Group findById(long id) {
        return em.find(Group.class,id);
    }

    public List<Group> findAll() {
        return em.createNamedQuery("group.findAll",Group.class)
                .getResultList();
    }
}
