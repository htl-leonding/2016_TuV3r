package at.htl.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Laurenz on 15.10.2015.
 */
@Entity
@XmlRootElement
@Table(name = "tv_round")
public class Round {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private long id;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Group group;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "round")
    private List<Match> matches;

    public Round(Group group, List<Match> matches) {
        this.group = group;
        this.matches = matches;
    }

    public Round() {
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
