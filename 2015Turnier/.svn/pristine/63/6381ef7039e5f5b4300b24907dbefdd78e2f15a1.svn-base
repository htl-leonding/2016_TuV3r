package at.htl.rest;

import at.htl.entity.Group;
import at.htl.entity.Match;
import at.htl.logic.GroupFacade;
import at.htl.logic.MatchFacade;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Laurenz on 12.11.2015.
 */
@Stateless
@Path("group")
public class GroupResource {
    @Inject
    GroupFacade groupFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Group findById(@PathParam("id") long id){
        return groupFacade.findById(id);
    }
    @GET
    @Produces({"application/xml","application/json"})
    public List<Group> findAll(){
        return groupFacade.findAll();
    }
}
