package org.scigap.vanillagateway.services;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.airavata.client.AiravataAPIFactory;
import org.apache.airavata.client.api.AiravataAPI;
import org.apache.airavata.client.api.exception.AiravataAPIInvocationException;
import org.apache.airavata.commons.gfac.type.ApplicationDescription;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.schemas.gfac.InputParameterType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
		try {
			AiravataAPI api = getAPI();
			List<ServiceDescription> applications = api.getApplicationManager()
					.getAllServiceDescriptions();
			for (ServiceDescription serviceDescription : applications) {
				application = new JSONObject();
				application.put("name", serviceDescription.getType().getName());
				jsonArray.add(application);
			}
		} catch (AiravataAPIInvocationException e) {
			e.printStackTrace();
		}
//		application=new JSONObject();
//		application.put("name", "US3AppTrestles");
//		jsonArray.add(application);
//		application=new JSONObject();
//		application.put("name", "US3AppStampede");
//		jsonArray.add(application);
//		application=new JSONObject();
//		application.put("name", "US3EchoTrestles");
//		jsonArray.add(application);
//		application=new JSONObject();
//		application.put("name", "US3EchoStampede");
//		jsonArray.add(application);
		return jsonArray.toString();
	}

	@Path("/{applicationId}/inputs")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getInputs(@PathParam("applicationId") String applicationId) {
//		if (applicationId.equals("US3AppTrestles")){
//			return "[{\"name\":\"input\",\"type\": \"URI\" }]";
//		} else if (applicationId.equals("US3AppStampede")){
//			return "[{\"name\":\"input\",\"type\": \"URI\" }]";
//		} else if (applicationId.equals("US3EchoTrestles")){
//			return "[{\"name\":\"echo_input\",\"type\": \"String\" }]";
//		} else if (applicationId.equals("US3EchoStampede")){
//			return "[{\"name\":\"echo_input\",\"type\": \"String\" }]";
//		}
//		return "[]";
		JSONArray jsonArray = new JSONArray();
		JSONObject application = null;
		try {
			AiravataAPI api = getAPI();
			ServiceDescription serviceDescription = api.getApplicationManager()
					.getServiceDescription(applicationId);
			InputParameterType[] inputs = serviceDescription.getType()
					.getInputParametersArray();
			for (InputParameterType inputParameterType : inputs) {
				application = new JSONObject();
				application.put("name", inputParameterType.getParameterName());
				application.put("type", inputParameterType.getParameterType().getName());
				jsonArray.add(application);
			}
		} catch (AiravataAPIInvocationException e) {
			e.printStackTrace();
		}
		return jsonArray.toString();
	}
	
	@Path("/{applicationId}/deployments")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getDeployments(@PathParam("applicationId") String applicationId) {
//		if (applicationId.equals("US3AppTrestles")){
//			return "[{\"name\":\"trestles\"}]";
//		} else if (applicationId.equals("US3AppStampede")){
//			return "[{\"name\":\"stampede\"}]";
//		} else if (applicationId.equals("US3EchoTrestles")){
//			return "[{\"name\":\"trestles\"}]";
//		} else if (applicationId.equals("US3EchoStampede")){
//			return "[{\"name\":\"stampede\"}]";
//		}
//		return "[]";
		JSONArray jsonArray = new JSONArray();
		JSONObject application = null;
		try {
			AiravataAPI api = getAPI();
			Map<String[], ApplicationDescription> allApplicationDescriptions = api.getApplicationManager().getAllApplicationDescriptions();
			for (String[] descriptorIds : allApplicationDescriptions.keySet()) {
				if (descriptorIds[0].equals(applicationId)){
					application = new JSONObject();
					application.put("name", descriptorIds[1]);
					jsonArray.add(application);
				}
			}
		} catch (AiravataAPIInvocationException e) {
			e.printStackTrace();
		}
		return jsonArray.toString();
	}
}
