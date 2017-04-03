package at.htl.entity;

import at.htl.logic.DateAdapter;
import at.htl.logic.GroupAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Lokal on 07.01.2016.
 */
@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(
                name = "tournament.findAllTournaments",
                query = "SELECT t from Tournament t order by t.id"
        )}
)
@Table(name = "tv_tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "to_id")
    private long id;
    @Column(name = "to_name")
    private String name;
    @Column(name="to_date")
    private LocalDate date;
    @Column(name = "to_isactive")
    private Boolean isActive;
    @Column(name = "to_pointswin")
    private int pointsWin;
    @Column(name = "to_pointsdraw")
    private int pointsDraw;
    @Column(name = "to_groupsize")
    private int groupSize;
    @Column(name = "to_groupphase")
    private Boolean groupPhase;
    @Column(name = "to_system")
    private String system;
    @Column(name = "to_placescount")
    private int placesCount;


    @OneToMany(mappedBy = "tournament")
    private Collection<Team> teams;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "tournament")
    private List<Match> matches;

    public Tournament(String name, LocalDate date, Boolean isActive, Collection<Team> teams) {
        this.name = name;
        this.date = date;
        this.isActive = isActive;
        this.teams = teams;
    }

    public Tournament(String name, LocalDate date, Boolean isActive, int pointsWin, int pointsDraw,
                      int groupSize, Boolean groupPhase, String system, Collection<Team> teams) {
        this.name = name;
        this.date = date;
        this.isActive = isActive;
        this.pointsWin = pointsWin;
        this.pointsDraw = pointsDraw;
        this.groupSize = groupSize;
        this.groupPhase = groupPhase;
        this.system = system;
        this.teams = teams;
    }

    public int getPlacesCount() {
        return placesCount;
    }

    public void setPlacesCount(int placesCount) {
        this.placesCount = placesCount;
    }

    public Tournament() {
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public LocalDate getDate() {
        return date;
    }




    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Team> getTeams() {
        return (List<Team>) teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public int getPointsWin() {
        return pointsWin;
    }

    public void setPointsWin(int pointsWin) {
        this.pointsWin = pointsWin;
    }

    public int getPointsDraw() {
        return pointsDraw;
    }

    public void setPointsDraw(int pointsDraw) {
        this.pointsDraw = pointsDraw;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public Boolean getGroupPhase() {
        return groupPhase;
    }

    public void setGroupPhase(Boolean groupPhase) {
        this.groupPhase = groupPhase;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setId(long id) {
        this.id = id;
    }
}
