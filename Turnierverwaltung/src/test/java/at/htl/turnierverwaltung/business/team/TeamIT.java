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























