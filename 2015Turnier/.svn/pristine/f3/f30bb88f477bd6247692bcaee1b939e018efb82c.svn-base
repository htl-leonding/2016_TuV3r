package at.htl.turnierverwaltung.business.team;

import at.htl.entity.Team;
import at.htl.logic.TournamentSystems;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Laurenz on 14.12.2015.
 */
public class TeamUnitIT {
    private Client client;
    private WebTarget target;
    EntityManager em;
    EntityManagerFactory emf;
    TournamentSystems systems;

    @Before
    public void initEm(){
        /*emf = Persistence.createEntityManagerFactory("MyPU");
        em = emf.createEntityManager();
        systems =new TournamentSystems();*/
        this.client = ClientBuilder.newClient();
        this.target=client.target("http://localhost:8080/Turnierverwaltung/rs/group");
    }
    @Test
    public void t001GroupsAddedTest(){
        Response response = this.target.request(MediaType.APPLICATION_JSON).get();
        Assert.assertThat(response.getStatus(),Is.is(200));
        JsonObject payLoad = response.readEntity(JsonObject.class);
        Assert.assertThat(payLoad.size(),Is.is(2));
    }
   /* @Test
    public void t001GroupAddedTest(){
        em.createQuery("Delete from Group");
        List<Team> teams = new ArrayList<Team>();
        em.getTransaction().begin();
        for(long i = 1; i<8+1;i++){
            Team team = new Team("Team"+i,false,0);
            em.persist(team);
            teams.add(team);
        }
        em.getTransaction().commit();
        systems.manageGroupPhase(teams);
        Query q= em.createNamedQuery("group.findAll");
        Assert.assertThat(q.getResultList().size(),Is.is(2));
    }
*/
    @Test
    public void t001addTeams(){
        /*
        JsonObjectBuilder teamBuilder = Json.createObjectBuilder();
        JsonObject teamToCreate = teamBuilder
                .add("id", "1")
                .add("name", "A")
                .add("hasLost",true)
                .add("rank",0)
                .build();
        Response response = this.target
                .request().post(Entity.json(teamToCreate));
        Assert.assertThat(response.getStatus(), Is.is(204));*/
        // weitere Tests
    }
}
