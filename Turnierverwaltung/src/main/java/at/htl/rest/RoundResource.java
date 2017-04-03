package at.htl.rest;

import at.htl.entity.Round;
import at.htl.entity.Team;
import at.htl.logic.RoundFacade;
import io.swagger.annotations.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by Lokal on 02.04.2017.
 */
@Path("round")
@Api(value = "/round",description = "Rounds")
public class RoundResource {
    @Inject
    RoundFacade roundFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get round by id")
    public Round findById(
            @ApiParam(value = "id of the wanted round", required = true)
            @PathParam("id") long id){
        return roundFacade.findById(id);
    }


    @PUT
    @Path("{id}")
    @ApiOperation(value = "Update a round")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Accepted"),
            @ApiResponse(code = 500, message = "Something went wrong in Server")})
    public Response update(
            @ApiParam(value = "id of the round that needs to be updated", required = true) @PathParam("id") long id,
            @ApiParam(value = "Updated round object in json", required = true) Round round){
        roundFacade.save(id,round);
        return Response.accepted().build();
    }
    @POST
    @ApiOperation(value = "Save a round")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Something went wrong in Server")})
    public Response save(
            @ApiParam(value = "the round as string (e.g. 1)", required = true)
                    String r, @Context UriInfo info){
        Round round = new Round(r,null);
        Round saved = roundFacade.save(0,round);
        URI uri = info.getAbsolutePathBuilder().path("/"+saved.getId()).build();
        return Response.created(uri).entity(Long.toString(saved.getId())).build();
    }
    @GET
    @ApiOperation(value = "Get all rounds")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<Round> findAll(){
        //return Response.ok(teamFacade.findAll()).build();
        return roundFacade.findAll();
    }
}
