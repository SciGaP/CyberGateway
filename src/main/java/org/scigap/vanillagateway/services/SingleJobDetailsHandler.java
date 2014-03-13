package org.scigap.vanillagateway.services;

import org.apache.airavata.model.workspace.experiment.DataObjectType;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.JobStatus;
import org.json.simple.JSONObject;
import org.scigap.vanillagateway.airavata.AiravataClient;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.print.attribute.standard.JobState;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/job")
public class SingleJobDetailsHandler {
    private AiravataClient client;
    @GET
    @Path("/{jobID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getExperiment(@PathParam(value = "jobID") final String jobID) throws IOException {
        //todo : test if two input files contain the same name because Angular doesn't allow that.

        if (client == null) {
            client = AiravataClient.getInstance();
        }

        Experiment experiment = client.getExperiment(jobID);
        Map<String, JobStatus> jobStatuses = client.getJobStatuses(jobID);
        String jobStatus = "NOT SUBMITTED";

        if (!jobStatuses.isEmpty()) {
            for (String key : jobStatuses.keySet()) {
                JobStatus jobstatus = jobStatuses.get(key);
                jobStatus = jobstatus.getJobState().toString();
            }
        }

        JSONObject job = new JSONObject();
        job.put("id", experiment.getExperimentID());
        job.put("name", experiment.getName());
        job.put("status", experiment.getExperimentStatus().getExperimentState().toString());
        job.put("project", experiment.getProjectID());
        job.put("description", experiment.getDescription());
        job.put("submitDate", convertTime(experiment.getCreationTime()));
        job.put("lastStatusUpdate", convertTime(experiment.getExperimentStatus().getTimeOfStateChange()));
        job.put("jobstatus", jobStatus);

        List<DataObjectType> experimentInputs = experiment.getExperimentInputs();


        return job.toString();
    }

    private String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date).toString();
    }
}
