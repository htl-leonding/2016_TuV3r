package at.htl.turnierverwaltung.business.team;

import at.htl.entity.Match;
import at.htl.entity.Result;
import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.TournamentSystems;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class TeamIT {
    @Inject
    TournamentSystems tournamentSystems;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Team.class, TournamentSystems.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void t01vehicleBurnTest() {
        Team team=new Team("Hartl4Life",false);
        Team team1 = new Team("HartlLover",false);
        int res = tournamentSystems.determineTotalPoints(team);
        Match match = new Match(false,team,team1,new Result(2,1));
        Assert.assertEquals(res,0);
        res = tournamentSystems.determineTotalPoints(team);
        Assert.assertEquals(res,3);
    }

    @Test
    public void t02leiterSystemTest()
    {
        TournamentSystems ts = new TournamentSystems();

        Team team1 = new Team("LASK Linz", false, 1);
        Team team2 = new Team("Blau-Weiß Linz", false, 2);
        Team team3 = new Team("Donau Linz", false, 3);
        Team team4 = new Team("SK ADmira Linz", false, 4);

        List<Team> teamList = new ArrayList<>();
        teamList.add(team1);
        teamList.add(team2);
        teamList.add(team3);
        teamList.add(team4);

        Result result1 = new Result("3:1");

        teamList = ts.getWinnerLeiterSystem(team2, team1, teamList, result1);

        Assert.assertEquals(teamList.get(1), team1);
    }

    /*@Test
    public void t02vehicleExtinguishTest() {
        Vehicle v = new Vehicle("Volkswagen", "Polo");

        Assert.assertEquals(burner.extinguish(v), "Ein Polo von der Marke Volkswagen");
    }

    @Test
    public void t03vehicleExtinguishTestRip() {
        Vehicle v = new Vehicle("Volkswagen", "Polo");

        Assert.assertEquals(burner.extinguish(v), "Ein Kommodore von der Marke Opel");
    }*/
}























