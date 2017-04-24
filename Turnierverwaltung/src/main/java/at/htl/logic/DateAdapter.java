package at.htl.logic;

import at.htl.entity.Group;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Hindert das rekursive Aufrufen des XML-Baumes
 * http://stackoverflow.com/questions/14569293/how-can-jaxb-xmladapter-be-used-to-marshall-lists
 */
public class DateAdapter extends XmlAdapter<String, LocalDate> {
    @PersistenceContext
    EntityManager em;

    public LocalDate unmarshal(final String xml) throws Exception {
        return LocalDate.parse(xml, DateTimeFormatter.ofPattern("dd.MM.YYYY"));
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
    }
}