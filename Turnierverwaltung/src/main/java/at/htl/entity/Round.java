package at.htl.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
    @Column(name = "r_count")
    private String count;
    @XmlTransient
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "round")
    private List<Match> matches;


    public Round(String count, List<Match> matches) {
        this.count = count;
        this.matches = matches;
    }

    public Round() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String roundCount) {
        this.count = roundCount;
    }

    @XmlTransient
    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @XmlTransient
    @ManyToOne(optional = true)
    @JoinColumn(name = "to_id")
    private Tournament tournament;
    @XmlTransient
    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}
