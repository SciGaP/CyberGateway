package org.scigap.vanillagateway.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/newjob")
public class NewJobHandler {
    private boolean formUploaded = false;
    private String name="not initialized",description="not initialized";

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String createJob(@FormParam("description") String description, @FormParam("name") String name) {
        this.name = name;
        this.description = description;
        formUploaded = true;
        return "Form uploading complete";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String isFormUploaded() {
        String form= formUploaded ? "form Uploaded" : "form not uploaded";
        return form + "name =" + this.name + " description = " + description;
    }

}
