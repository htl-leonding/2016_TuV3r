package at.htl.rest;

import at.htl.entity.Group;
import at.htl.entity.Match;
import at.htl.logic.GroupFacade;
import at.htl.logic.MatchFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
//import com.wordnik.swagger.annotations.*;

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
@Api(value = "/group",description = "Groups")
public class GroupResource {
    @Inject
    GroupFacade groupFacade;

    @GET
    @Path("{id}")
    @ApiOperation(value = "get a group by id")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Group findById(
            @ApiParam(value = "id of the wanted group", required = true)
            @PathParam("id") long id){
        return groupFacade.findById(id);
    }
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get all groups")
    public List<Group> findAll(){
        return groupFacade.findAll();
    }
}
