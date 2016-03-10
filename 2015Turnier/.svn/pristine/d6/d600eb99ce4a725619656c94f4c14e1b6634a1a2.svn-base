package at.htl.rest;

import at.htl.entity.Match;
import at.htl.logic.MatchFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Laurenz on 12.11.2015.
 */
@Path("match")
public class MatchResource {
    @Inject
    MatchFacade matchFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Match findById(@PathParam("id") long id){
        return matchFacade.findById(id);
    }
    @GET
    @Produces({"application/xml","application/json"})
    public List<Match> findAll(){
        return matchFacade.findAll();
    }
}
