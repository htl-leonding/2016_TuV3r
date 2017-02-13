package at.htl.rest;

import at.htl.entity.Team;
import at.htl.logic.TeamFacade;
import io.swagger.annotations.*;
//import com.wordnik.swagger.annotations.*;

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
@Api(value = "/team",description = "Teams")
public class TeamResource {
    @Inject
    TeamFacade teamFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get team by id")
    public Team findById(
            @ApiParam(value = "id of the wanted team", required = true)
            @PathParam("id") long id){
        return teamFacade.findById(id);
    }


    @PUT
    @Path("{id}")
    @ApiOperation(value = "Update a team")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Accepted"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response update(
            @ApiParam(value = "id of the team that needs to be updated", required = true) @PathParam("id") long id,
            @ApiParam(value = "Updated team object in json", required = true) Team team){
        teamFacade.save(id,team);
        return Response.accepted().build();
    }
    @POST
    @ApiOperation(value = "Save a team")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response save(
            @ApiParam(value = "the new team in json", required = true)
            Team team, @Context UriInfo info){
        Team saved = teamFacade.save(0,team);
        URI uri = info.getAbsolutePathBuilder().path("/"+saved.getId()).build();
        return Response.created(uri).build();
    }
    @GET
    @ApiOperation(value = "Get all teams")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<Team> findAll(){
        //return Response.ok(teamFacade.findAll()).build();
        return teamFacade.findAll();
    }
}
