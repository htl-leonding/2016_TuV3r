package at.htl.rest;

import at.htl.entity.Dto;
import at.htl.entity.Match;
import at.htl.entity.Result;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
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
@Path("match")
@Api(value = "/match",description = "Matches")
public class MatchResource {
    @Inject
    MatchFacade matchFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get match by id")
    public Match findById(
            @ApiParam(value = "id of the wanted match", required = true)
            @PathParam("id") long id){
        return matchFacade.findById(id);
    }
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get all matches")
    public List<Match> findAll(){
        return matchFacade.findAll();
    }
    @GET
    @Path("by")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get matches by tournament or team id, only one queryparam can be set at once")
    public List<Match> findByTournamentId(
            @ApiParam(value = "id of the tournament the wanted matches are in")
            @QueryParam("toid") long tournamentId,
            @ApiParam(value = "id of the team you want the matches of")
            @QueryParam("teid") long teamId){
        if(tournamentId>0) {
            return matchFacade.findMatchesByTournamentId(tournamentId);
        } else if(teamId>0){
            return matchFacade.findMatchesByTeamId(teamId);
        }
        return null;
    }
    @PUT
    @Path("{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Accepted"),
            @ApiResponse(code = 500, message = "Something went wrong in Server")})
    @ApiOperation(value = "Update a match")
    public Response update(
            @ApiParam(value = "Updated match object in json", required = true)
                    Match m,
            @ApiParam(value = "id of the match that needs to be updated", required = true)
            @PathParam(value = "id") long id){
        matchFacade.save(id,m);
        return Response.accepted().build();
    }
    @POST
    @ApiOperation(value = "Save a match")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 500, message = "Something went wrong in Server")})
    public Response save(
            @ApiParam(value = "new MatchDto in json, consisting of a resultobject, and the two team-ids"
                    ,required = true)
            Dto dto){
        matchFacade.saveByIds(dto.getResult(),dto.getTeam1Id(),dto.getTeam2Id());
        return Response.ok().build();
        //matchFacade.save(0,m);
    }
}
