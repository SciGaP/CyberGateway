package org.scigap.cybergateway.services;

import javax.ws.rs.Path;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.airavata.model.workspace.experiment.Experiment;
import org.codehaus.jettison.json.JSONException;
import org.json.simple.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.scigap.cybergateway.airavata.AiravataClient;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/alljobs")
public class AllJobDetailsHandler {
    private AiravataClient client;
    private static Set<String> projects =null;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllExperiments() {
        projects = new HashSet<String>();

        if (client == null) {
            client = AiravataClient.getInstance();
        }

        //fixme use the real username
        List<Experiment> experiments = client.getAllExperiments("admin");

        JSONObject job_json = null;
        JSONArray jsonArray = new JSONArray();

        Map<String, String> job = null;
        if (experiments == null) {
            job = new HashMap<String, String>();
            job.put("id", "No Experiments yet");
            job_json = new JSONObject(job);
            jsonArray.add(job_json);
            return jsonArray.toString();
        }

        for (Experiment experiment : experiments) {
            job = new HashMap<String, String>();

            job.put("id", experiment.getExperimentID());
            job.put("name", experiment.getName());
            job.put("username", experiment.getUserName());
            job.put("status", experiment.getExperimentStatus().getExperimentState().toString());
            job.put("project", experiment.getProjectID());
            projects.add(experiment.getProjectID());

            job.put("description", experiment.getDescription());
            job.put("submitDate", convertTime(experiment.getCreationTime()));
            job.put("lastStatusUpdate", convertTime(experiment.getExperimentStatus().getTimeOfStateChange()));

            job_json = new JSONObject(job);
            jsonArray.add(job_json);
        }

        return jsonArray.toString();
    }

    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date).toString();
    }

    @GET
    @Path("/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllProjects() {
        getAllExperiments();

        if(projects == null) {
            return "no projects";
        }
        JSONObject job_json = new JSONObject();

        //adding all experiments
        try {
            job_json.put("name", "AllExperiments");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(job_json);

        //adding rest of the projects
        for (String project : projects) {
            job_json = new JSONObject();
            try {
                job_json.put("name", project);
                jsonArray.add(job_json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return jsonArray.toString();
    }

}
