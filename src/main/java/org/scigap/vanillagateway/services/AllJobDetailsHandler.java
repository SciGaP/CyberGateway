package org.scigap.vanillagateway.services;

import javax.ws.rs.Path;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.ExperimentState;
import org.apache.airavata.model.workspace.experiment.ExperimentStatus;
import org.json.simple.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.scigap.vanillagateway.airavata.AiravataClient;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/alljobs")
public class AllJobDetailsHandler {
    private AiravataClient client;

    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllJobs() throws IOException {
        Map<String, String> job3 = new HashMap<String, String>();
        job3.put("id", "j3");
        job3.put("name", "Job three");
        job3.put("machine", "Mason");
        job3.put("status", "Finished");
        job3.put("lastRunTime", "01232014");
        job3.put("project", "Airavata");
        job3.put("description", "This is a test description");

        Map<String, String> job4 = new HashMap<String, String>();
        job4.put("id", "j4");
        job4.put("name", "Job four");
        job4.put("machine", "Big Red II");
        job4.put("status", "Queued");
        job4.put("lastRunTime", "993399");
        job4.put("project", "Protein");
        job4.put("description", "Hello Test");

        Map<String, String> job5 = new HashMap<String, String>();
        job5.put("id", "j5");
        job5.put("name", "Test Experiment");
        job5.put("machine", "Quarry");
        job5.put("status", "Launched");
        job5.put("lastRunTime", "0123442014");
        job5.put("project", "Protein");
        job5.put("description", "Protein working test");

        Map<String, String> job6 = new HashMap<String, String>();
        job6.put("id", "j6");
        job6.put("name", "r2pg0-119 Exp");
        job6.put("machine", "Big Red II");
        job6.put("status", "Finished");
        job6.put("lastRunTime", "012332014");
        job6.put("project", "Cybergateway");
        job6.put("description", "Quary test project");

        Map<String, String> job7 = new HashMap<String, String>();
        job7.put("id", "j7");
        job7.put("name", "Airavata Tester");
        job7.put("machine", "Big Red II");
        job7.put("status", "Queued");
        job7.put("lastRunTime", "0123442014");
        job7.put("project", "Cybergateway");
        job7.put("description", "bigred 2 tester");

        JSONObject job3_json = new JSONObject(job3);
        JSONObject job4_json = new JSONObject(job4);
        JSONObject job5_json = new JSONObject(job5);
        JSONObject job6_json = new JSONObject(job6);
        JSONObject job7_json = new JSONObject(job7);
        JSONArray jsonArray = new JSONArray();

        jsonArray.add(job3_json);
        jsonArray.add(job4_json);
        jsonArray.add(job5_json);
        jsonArray.add(job6_json);
        jsonArray.add(job7_json);

        return jsonArray.toString();
    }
*/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllExperiments() {
        if (client == null) {
            client = AiravataClient.getInstance();
        }

        //fixme use the real username
        List<Experiment> experiments = client.getAllExperiments("admin");

        JSONObject job_json = null;
        JSONArray jsonArray = new JSONArray();

        Map<String, String> job = null;
        if(experiments == null){
            job.put("id", "No Experiments yet");
            job_json = new JSONObject(job);
            jsonArray.add(job_json);
            return jsonArray.toString();
        }

        for (Experiment experiment : experiments) {
            job = new HashMap<String, String>();

            job.put("id", experiment.getExperimentID());
            job.put("name", experiment.getName());
            //job.put("machine", "Mason");
            //fixme get the real status
//            ExperimentStatus experimentStatus = experiment.getExperimentStatus();
            //ExperimentState experimentState = experimentStatus.getExperimentState();
            //long timeOfStateChange = experimentStatus.getTimeOfStateChange();

            //job.put("status", experimentState.toString());
            job.put("project", experiment.getProjectID());
            job.put("description", experiment.getDescription());
            job.put("submitDate", convertTime(experiment.getCreationTime()));
            //job.put("lastStatusUpdate", convertTime(timeOfStateChange));

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

}
