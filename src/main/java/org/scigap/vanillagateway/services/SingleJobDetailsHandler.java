package org.scigap.vanillagateway.services;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/job")
public class SingleJobDetailsHandler {

	@GET
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

        return job_json.toString();
	}
}
