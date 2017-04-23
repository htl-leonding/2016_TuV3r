package at.htl.logic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Hindert das rekursive Aufrufen des XML-Baumes
 * http://stackoverflow.com/questions/14569293/how-can-jaxb-xmladapter-be-used-to-marshall-lists
 */
public class TimeAdapter extends XmlAdapter<String, LocalTime> {
    @PersistenceContext
    EntityManager em;

    @Override
    public LocalTime unmarshal(String v) throws Exception {
        return LocalTime.parse(v, DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return v.toString();
    }
}