package org.scigap.vanillagateway.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.scigap.vanillagateway.airavata.AiravataClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by swithana on 3/12/14.
 */
@Path("/applications")
public class ApplicationHandler {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getExperiment() {

        JSONArray jsonArray = new JSONArray();
        JSONObject application = null;
        String[] applications= {"SimpleEcho3","UltraScan","CIPRES"};

        for(int i = 0; i<applications.length;i++) {
            application = new JSONObject();
            application.put("name", applications[i]);
            jsonArray.add(application);
        }

        return jsonArray.toString();

    }
}
