package at.htl.rest;

/*
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
*/

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("rs")
public class RestConfig extends Application {
    public RestConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        //beanConfig.setHost("vm15.htl-leonding.ac.at:8090");
        beanConfig.setBasePath("/Turnierverwaltung/rs");
        beanConfig.setResourcePackage("at.htl.rest");
        beanConfig.setScan(true);
    }
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        /*resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);*/
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);

        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(at.htl.rest.TournamentResource.class);
        resources.add(at.htl.rest.GroupResource.class);
        resources.add(at.htl.rest.MatchResource.class);
        resources.add(at.htl.rest.TeamResource.class);
        resources.add(at.htl.rest.RoundResource.class);
    }

}

