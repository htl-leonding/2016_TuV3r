package at.htl.logic;

import at.htl.entity.Group;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hindert das rekursive Aufrufen des XML-Baumes
 * http://stackoverflow.com/questions/14569293/how-can-jaxb-xmladapter-be-used-to-marshall-lists
 */
public class GroupAdapter extends XmlAdapter<String, Group> {
    @PersistenceContext
    EntityManager em;

    //holt sich das Objekt mithilfe das Namen
    public Group unmarshal(final String xml) throws Exception {
        return em.find(Group.class, Long.parseLong(xml));
    }

    @Override
    public String marshal(Group v) throws Exception {
        return v.getName();
    }
}