package at.htl.entity;

import at.htl.logic.GroupAdapter;
import at.htl.logic.TournamentSystems;

import javax.inject.Inject;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Laurenz on 02.10.2015.
 */
@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "team.findAll",
                query = "select t from Team t")
})

@Table(name = "tv_team")
public class Team{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id")
    private long id;
    @Column(name = "t_name")
    private String name;
    //win-draw-lose
    @Column(name ="t_hasLost")
    private boolean hasLost;
    @Column(name ="t_rank")
    private int rank;
    @Column(name ="t_occupied")
    private boolean occupied;


    //region Constructor
    public Team() {
    }

    public Team(String name, boolean hasLost, int rank) {
        this.name = name;
        this.hasLost = hasLost;
        this.rank = rank;
    }

    public Team(String name, boolean hasLost) {
        this.name = name;
        this.hasLost = hasLost;
    }
    public Team(String name, boolean hasLost,Tournament tournament) {
        this.name = name;
        this.hasLost = hasLost;
        this.tournament= tournament;
    }

    //endregion
    //region Getter & Setter

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public boolean HasLost() {
        return hasLost;
    }

    public void setHasLost(boolean hasLost) {
        this.hasLost = hasLost;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    //endregion


    @Override
    public String toString() {
        return "Name: " + name + ", Id: " + id;
    }

    @XmlTransient
    @ManyToOne(optional = true)
    @JoinColumn(name = "g_id")
    private Group group;

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

    @XmlJavaTypeAdapter(GroupAdapter.class)
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}