package org.scigap.vanillagateway.services;

import org.apache.airavata.model.workspace.experiment.DataObjectType;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.json.simple.JSONObject;
import org.scigap.vanillagateway.airavata.AiravataClient;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/job")
public class SingleJobDetailsHandler {
    private AiravataClient client;

  /*  @GET
    @Path("/{jobID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDummyJob(@PathParam(value = "jobID") final String jobID) throws IOException {
        //todo : test if two input files contain the same name because Angular doesn't allow that.

        List<String> inputs = new ArrayList<String>();
        List<String> intermediateFiles = new ArrayList<String>();
        List<String> outputs = new ArrayList<String>();

        inputs.add("test.trt");
        inputs.add("lastInput.java");

        intermediateFiles.add("lastintermid.java");
        intermediateFiles.add("test.class");

        outputs.add("output1.java");
        outputs.add("output2.csv");

        JSONObject job_json = new JSONObject();
        job_json.put("id", jobID);
        job_json.put("name", "Airavata Tester");
        job_json.put("resource", "Big Red II");
        job_json.put("status", "Queued");
        job_json.put("createdDate", "01_14_2014");
        job_json.put("inputs", inputs);
        job_json.put("intermediateFiles", intermediateFiles);
        job_json.put("outputs", outputs);
        job_json.put("statusUpdateTime", "test time");

        return job_json.toString();
    }*/

    @GET
    @Path("/{jobID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getExperiment(@PathParam(value = "jobID") final String jobID) throws IOException {
        //todo : test if two input files contain the same name because Angular doesn't allow that.

        if (client == null) {
            client = AiravataClient.getInstance();
        }

        Experiment experiment = client.getExperiment(jobID);

        JSONObject job = new JSONObject();
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

        List<DataObjectType> experimentInputs = experiment.getExperimentInputs();
        

        return job.toString();
    }
    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date).toString();
    }
}
