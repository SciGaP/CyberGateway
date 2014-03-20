package org.scigap.cybergateway.services;

import org.scigap.cybergateway.airavata.AiravataClient;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/cancel")
public class CancelJob {
    private AiravataClient client;


    @GET
    @Path("/{expID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getExperiment(@PathParam(value = "expID") final String expID) throws IOException {
        //todo : test if two input files contain the same name because Angular doesn't allow that.

        if (client == null) {
            client = AiravataClient.getInstance();
        }

        client.cancelExperiment(expID);

        return "";

    }
}
