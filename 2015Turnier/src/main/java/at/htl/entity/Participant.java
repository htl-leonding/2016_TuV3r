package at.htl.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Laurenz on 02.10.2015.
 */
@Entity
@XmlRootElement
@Table(name = "tv_participant")
public class Participant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private long id;
    @Column(name = "p_fname")
    private String fName;
    @Column(name = "p_lname")
    private String lName;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "t_id")
    private Team team;

    //region Constructor
    public Participant() {
    }

    public Participant(String fName, String lName, Team team) {
        this.fName = fName;
        this.lName = lName;
        this.team = team;
    }
    //endregion
    //region Getter & Setter
    public String getvFName() {
        return fName;
    }

    public void setvFName(String vName) {
        this.fName = vName;
    }

    public String getnLName() {
        return lName;
    }

    public void setnLName(String nName) {
        this.lName = nName;
    }

    public long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    //endregion


    @Override
    public String toString() {
        return "id: "+id+", Vorname: "+ fName +", Nachname: "+ lName;
    }
}
