package at.htl.rest;

import at.htl.entity.Tournament;
import at.htl.logic.TournamentFacade;
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
 * Created by Lokal on 09.01.2017.
 */
@Path("tournament")
@Api(value = "/tournament",description = "Tournament")
public class TournamentResource
{
    @Inject
    TournamentFacade tournamentFacade;

    @GET
    @Path("{id}")
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    //Date is only shown in the json format, when using xml, the date field is empty
    @ApiOperation(value = "Get tournament by id")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Tournament findById(
            @ApiParam(value = "id of the wanted tournament", required = true)
            @PathParam("id") long id){
        return tournamentFacade.findById(id);
    }

    @PUT
    @Path("{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Accepted"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    @ApiOperation(value = "Update a tournament")
    public Response update(
            @ApiParam(value = "Updated tournament object in json", required = true)
            Tournament t,
            @ApiParam(value = "id of the tournament that needs to be updated", required = true)
            @PathParam(value = "id") long id){
        tournamentFacade.save(id,t);
        return Response.accepted().build();
    }
    @POST
    @ApiOperation(value = "Save a tournament")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Created"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response save(
            @ApiParam(value = "new tournament in json",required = true)
            Tournament t,
            @Context UriInfo info){
        Tournament saved = tournamentFacade.save(0, t);
        URI uri = info.getAbsolutePathBuilder().path("/"+saved.getId()).build();
        return Response.created(uri).build();
    }
    @GET
    @ApiOperation(value = "Get all tournaments")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<Tournament> findAll(){
        return tournamentFacade.findAllTournaments();
    }
}
