package org.scigap.vanillagateway.services;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;
import java.util.List;

@Path("/newjob")
public class NewJobHandler {
    private String name = "not initialized", description = "not initialized";

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String createJob(@FormParam("name") String name,@FormParam("description") String description) {

        return "Form uploading complete   " + "name= " + name + " description= " + description;
    }

}
