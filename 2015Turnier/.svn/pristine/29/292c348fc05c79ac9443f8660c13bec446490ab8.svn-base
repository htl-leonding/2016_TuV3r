package at.htl.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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

    @OneToMany(mappedBy = "tournament")
    private Collection<Team> teams;

    public Tournament(String name, LocalDate date, Boolean isActive, Collection<Team> teams) {
        this.name = name;
        this.date = date;
        this.isActive = isActive;
        this.teams = teams;
    }

    public Tournament() {
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

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
}
