package at.htl.rest;

import at.htl.entity.Tournament;
import at.htl.logic.TournamentFacade;

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
public class TournamentResource
{
    @Inject
    TournamentFacade tournamentFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Tournament findById(@PathParam("id") long id){
        return tournamentFacade.findById(id);
    }

    @PUT
    @Path("{id}")
    public Response update(Tournament t, @PathParam(value = "id") long id){
        t.setId(id);
        tournamentFacade.save(t);
        return Response.accepted().build();
    }
    @POST
    public Response save(Tournament t, @Context UriInfo info){
        Tournament saved = tournamentFacade.save(t);
        URI uri = info.getAbsolutePathBuilder().path("/"+saved.getId()).build();
        return Response.created(uri).build();
    }
    @GET
    @Produces({"application/xml","application/json"})
    public List<Tournament> findAll(){
        return tournamentFacade.findAllTournaments();
    }
}
