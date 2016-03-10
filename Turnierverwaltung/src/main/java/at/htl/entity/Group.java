package at.htl.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Laurenz on 15.10.2015.
 */
@Entity
@XmlRootElement
@NamedQuery(name = "group.findAll",
        query = "select g from Group g")
@Table(name = "tv_group")
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_id")
    private long id;
    @Column(name = "g_name")
    private String name;
    /*@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "group")
    private List<Round> round;
*/

    public Group(String name, List<Team> teams) {
        this.name = name;
        this.teams = new ArrayList<Team>(teams);
    }

    public Group() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Team> getTeams() {
        return (List)teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "Gruppe "+name;
    }

    @OneToMany(mappedBy = "group")
    private Collection<Team> teams;

    public long getId() {
        return id;
    }
}
