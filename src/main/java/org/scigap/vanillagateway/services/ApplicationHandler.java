package org.scigap.vanillagateway.services;

import org.apache.airavata.client.AiravataAPIFactory;
import org.apache.airavata.client.api.AiravataAPI;
import org.apache.airavata.client.api.exception.AiravataAPIInvocationException;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.schemas.gfac.InputParameterType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.scigap.vanillagateway.airavata.AiravataClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.List;

/**
 * Created by swithana on 3/12/14.
 */
@Path("/applications")
public class ApplicationHandler {
	private static AiravataAPI api = null;

	public static AiravataAPI getAPI() throws AiravataAPIInvocationException {
		if (api == null) {
			api = AiravataAPIFactory.getAPI("default", "admin");
		}
		return api;
	}

	@Path("/list")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getExperiment() {
		JSONArray jsonArray = new JSONArray();
		JSONObject application = null;
//		try {
//			AiravataAPI api = getAPI();
//			List<ServiceDescription> applications = api.getApplicationManager()
//					.getAllServiceDescriptions();
//			for (ServiceDescription serviceDescription : applications) {
//				application = new JSONObject();
//				application.put("name", serviceDescription.getType().getName());
//				jsonArray.add(application);
//			}
//		} catch (AiravataAPIInvocationException e) {
//			e.printStackTrace();
//		}
		application=new JSONObject();
		application.put("name", "echo-app");
		jsonArray.add(application);
		application=new JSONObject();
		application.put("name", "us3-app");
		jsonArray.add(application);
		application=new JSONObject();
		application.put("name", "ls-app");
		jsonArray.add(application);
		return jsonArray.toString();
	}

	@Path("/{applicationId}/inputs")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getInputs(@PathParam("applicationId") String applicationId) {
		if (applicationId.equals("echo-app")){
			return "[{\"name\":\"echo_input\",\"type\": \"String\" }]";
		} else if (applicationId.equals("ls-app")){
			return "[{\"name\":\"path\",\"type\": \"String\" },{\"name\":\"options\",\"type\": \"String\" }]";
		} else if (applicationId.equals("us3-app")){
			return "[{\"name\":\"dataset\",\"type\": \"File\" }]";
		}
		return "[]";
//		JSONArray jsonArray = new JSONArray();
//		JSONObject application = null;
//		try {
//			AiravataAPI api = getAPI();
//			ServiceDescription serviceDescription = api.getApplicationManager()
//					.getServiceDescription(applicationId);
//			InputParameterType[] inputs = serviceDescription.getType()
//					.getInputParametersArray();
//			for (InputParameterType inputParameterType : inputs) {
//				application = new JSONObject();
//				application.put("name", inputParameterType.getParameterName());
//				application.put("type", inputParameterType.getParameterType());
//				jsonArray.add(application);
//			}
//		} catch (AiravataAPIInvocationException e) {
//			e.printStackTrace();
//		}
//		return jsonArray.toString();
	}
	
	@Path("/{applicationId}/deployments")
	@GET
	@Produces("text/html")
	public String getDeployments(@PathParam("applicationId") String applicationId) {
		if (applicationId.equals("echo-app")){
			return "[{\"name\":\"trestles\"}]";
		} else if (applicationId.equals("ls-app")){
			return "[{\"name\":\"stampede\"},{\"name\":\"bigred\"}]";
		} else if (applicationId.equals("us3-app")){
			return "[{\"name\":\"trestles\"},{\"name\":\"stampede\"}]";
		}
		return "[]";
//		JSONArray jsonArray = new JSONArray();
//		JSONObject application = null;
//		try {
//			AiravataAPI api = getAPI();
//			ServiceDescription serviceDescription = api.getApplicationManager()
//					.getServiceDescription(applicationId);
//			InputParameterType[] inputs = serviceDescription.getType()
//					.getInputParametersArray();
//			for (InputParameterType inputParameterType : inputs) {
//				application = new JSONObject();
//				application.put("name", inputParameterType.getParameterName());
//				application.put("type", inputParameterType.getParameterType());
//				jsonArray.add(application);
//			}
//		} catch (AiravataAPIInvocationException e) {
//			e.printStackTrace();
//		}
//		return jsonArray.toString();
	}
}
