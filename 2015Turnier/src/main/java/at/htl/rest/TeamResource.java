package at.htl.rest;

import at.htl.entity.Team;
import at.htl.logic.TeamFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by Laurenz on 12.11.2015.
 */
@Path("team")
public class TeamResource {
    @Inject
    TeamFacade teamFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team findById(@PathParam("id") long id){
        return teamFacade.findById(id);
    }


    @PUT
    @Path("{id}")
    public Response update(Team team, @PathParam(value = "id") long id){
        team.setId(id);
        //teamFacade.update(team);
        return Response.accepted().build();
    }
    @POST
    public Response save(Team team, @Context UriInfo info){
        Team saved = teamFacade.save(team);
        URI uri = info.getAbsolutePathBuilder().path("/"+saved.getId()).build();
        return Response.created(uri).build();
    }
    @GET
    @Produces({"application/xml","application/json"})
    public List<Team> findAll(){
        return teamFacade.findAll();
    }
}
