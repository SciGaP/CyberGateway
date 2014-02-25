package org.scigap.vanillagateway.services;

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

import org.codehaus.jettison.json.JSONObject;

@Path("/job")
public class SingleJobDetailsHandler {

	@GET
	@Path("/{jobID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDummyJob(@PathParam(value = "jobID") final String jobID) throws IOException {

		Map<String, Object> job = new HashMap<String, Object>();
		job.put("id", jobID);
		job.put("name", "Airavata Tester");
		job.put("resource", "Big Red II");
		job.put("status", "Queued");
		job.put("createdDate", "01_14_2014");

		//todo : test if two input files contain the same name because Angular doesn't allow that.

		List<String> inputs = new ArrayList<String>();
		List<String> intermediateFiles = new ArrayList<String>();
		List<String> outputs = new ArrayList<String>();

		inputs.add("test.trt");
		inputs.add("lastInput.java");

		intermediateFiles.add("lastintermid.java");
		intermediateFiles.add("lastIntermid.class");

		outputs.add("output1.java");
		outputs.add("output2.csv");


		job.put("inputs", inputs);
		job.put("intermediateFiles", intermediateFiles);
		job.put("outputs", outputs);

		JSONObject job_json = new JSONObject(job);

		return job_json.toString();
	}
}
