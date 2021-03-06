package org.scigap.cybergateway.services;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.apache.airavata.model.workspace.experiment.ComputationalResourceScheduling;
import org.apache.airavata.model.workspace.experiment.DataObjectType;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.UserConfigurationData;
import org.scigap.cybergateway.airavata.AiravataClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Path("/newjob")
public class NewJobHandler {
    private String name = "not initialized", description = "not initialized";
    private AiravataClient client;

    @POST
    @Path("/submit")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String createJob(@FormDataParam("file") InputStream uploadedInputStream,
                            @FormDataParam("file") FormDataContentDisposition fileDetail,
                            @FormDataParam("name") final String name,
                            @FormDataParam("application") final String application,
                            @FormDataParam("deployment") final String host,
                            @FormDataParam("project") final String project,
                            @FormDataParam("description") final String description) {


        //for stampede
        Experiment experiment = createExperiment(project, "admin", name, description, application, host, null);
        String experimentId = submitJob(experiment);


        //downloading the files to the server
        // String uploadedFileLocation = "/Users/swithana/temp/" + fileDetail.getFileName();
        //saveToFile(uploadedInputStream, uploadedFileLocation);

//        return application;
        return "Job Created Successfully" + " \nExperiment Name: " + name + "\nDescription: " + description +
                "\nExperimentID = " + experimentId;
    }

    @POST
    @Path("/save")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String saveExperiment(@FormDataParam("file") InputStream uploadedInputStream,
                                 @FormDataParam("file") FormDataContentDisposition fileDetail,
                                 @FormDataParam("name") final String name,
                                 @FormDataParam("application") final String application,
                                 @FormDataParam("deployment") final String host,
                                 @FormDataParam("project") final String project,
                                 @FormDataParam("description") final String description) {


        //for stampede
        Experiment experiment = createExperiment(project, "admin", name, description, application, host, null);
        String experimentId = saveExperiment(experiment);


        //downloading the files to the server
        // String uploadedFileLocation = "/Users/swithana/temp/" + fileDetail.getFileName();
        //saveToFile(uploadedInputStream, uploadedFileLocation);

//        return application;
        return "Job saved Successfully" + " \nExperiment Name: " + name + "\nDescription: " + description +
                "\nExperimentID = " + experimentId;
    }

    @POST
    @Path("/launch")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String launchSavedExperiment(@FormDataParam("expID") final String expID) {


        launchExperiment(expID);


        //downloading the files to the server
        // String uploadedFileLocation = "/Users/swithana/temp/" + fileDetail.getFileName();
        //saveToFile(uploadedInputStream, uploadedFileLocation);

//        return application;
        return "Experiment launched successfully "+expID;
    }

    //##################### Thrift client calls #######################
    private String submitJob(Experiment experiment) {
        if (client == null) {
            client = AiravataClient.getInstance();
        }
        return client.submitJob(experiment);
    }

    private String saveExperiment(Experiment experiment) {
        if (client == null) {
            client = AiravataClient.getInstance();
        }
        return client.saveExperiment(experiment);
    }

    private void launchExperiment(String experimentId) {
        if (client == null) {
            client = AiravataClient.getInstance();
        }
        client.launchExperiment(experimentId);
    }

    //#################################################################


    // FIXME get the inputs and scheduling from the client
    private Experiment createExperiment(String projectID,
                                        String userName,
                                        String experimentName,
                                        String expDescription,
                                        String applicationId, String host,
                                        List<DataObjectType> experimentInputList) {
        Experiment experiment = new Experiment();
        experiment.setProjectID(projectID);
        experiment.setUserName(userName);
        experiment.setName(experimentName);
        experiment.setDescription(expDescription);
        experiment.setApplicationId(applicationId);

        List<DataObjectType> exInputs = getInputPatameterList(applicationId);
        experiment.setExperimentInputs(exInputs);

        List<DataObjectType> exOut = getOutputParameterList(applicationId);
        experiment.setExperimentOutputs(exOut);

        UserConfigurationData userConfigurationData = getUserConfigurationData(host);
        experiment.setUserConfigurationData(userConfigurationData);

        return experiment;
    }

    private List<DataObjectType> getOutputParameterList(String appID) {
        List<DataObjectType> exOut = null;
        if (appID.contains("App")) {
            exOut = new ArrayList<DataObjectType>();
            DataObjectType output = new DataObjectType();
            output.setKey("output");
            output.setValue("");
            DataObjectType output1 = new DataObjectType();
            output1.setKey("stdout");
            output1.setValue("");
            DataObjectType output2 = new DataObjectType();
            output2.setKey("stderr");
            output2.setValue("");
            exOut.add(output);
            exOut.add(output1);
            exOut.add(output2);
        } else {
            exOut = new ArrayList<DataObjectType>();
            DataObjectType output = new DataObjectType();
            output.setKey("echo_output");
            output.setValue("");
            exOut.add(output);
        }
        return exOut;
    }

    private List<DataObjectType> getInputPatameterList(String appID) {
        List<DataObjectType> exInputs = null;
        if (appID.contains("App")) {
            exInputs = new ArrayList<DataObjectType>();
            DataObjectType input = new DataObjectType();
            input.setKey("input");
            input.setValue("file:///home/airavata/input/hpcinput.tar");
            exInputs.add(input);
        } else {
            exInputs = new ArrayList<DataObjectType>();
            DataObjectType input = new DataObjectType();
            input.setKey("echo_input");
            input.setValue("echo_output=Hello World");
            exInputs.add(input);
        }

        return exInputs;
    }

    private UserConfigurationData getUserConfigurationData(String host) {
        ComputationalResourceScheduling scheduling = getComputationalResourceScheduling(host);
        UserConfigurationData userConfigurationData = new UserConfigurationData();
        userConfigurationData.setAiravataAutoSchedule(false);
        userConfigurationData.setOverrideManualScheduledParams(false);
        userConfigurationData.setComputationalResourceScheduling(scheduling);
        return userConfigurationData;
    }

    private ComputationalResourceScheduling getComputationalResourceScheduling(String host) {
        ComputationalResourceScheduling scheduling = null;
        if (host.contains("stampede")) {
            scheduling =
                    ExperimentModelUtil.createComputationResourceScheduling("stampede.tacc.xsede.org", 1, 1, 1, "normal", 0, 0, 1, "TG-MCB070039N");
            scheduling.setResourceHostId("gsissh-stampede");
        } else if (host.contains("trestles")) {
            scheduling = createComputationResourceScheduling("trestles.sdsc.edu", 2, 32, 0, "shared", 0, 0, 0, "uot111");
            scheduling.setResourceHostId("gsissh-trestles");
        } else {
            scheduling =
                    ExperimentModelUtil.createComputationResourceScheduling("stampede.tacc.xsede.org", 1, 1, 1, "normal", 0, 0, 1, "TG-MCB070039N");
            scheduling.setResourceHostId("gsissh-stampede");
        }
        return scheduling;
    }

    private ComputationalResourceScheduling createComputationResourceScheduling(String resourceHostId,
                                                                                int cpuCount,
                                                                                int nodeCount,
                                                                                int numberOfThreads,
                                                                                String queueName,
                                                                                int wallTimeLimit,
                                                                                long jobstartTime,
                                                                                int totalPhysicalMemory,
                                                                                String projectAccount) {

        ComputationalResourceScheduling cmRS = new ComputationalResourceScheduling();
        cmRS.setResourceHostId(resourceHostId);
        cmRS.setTotalCPUCount(cpuCount);
        cmRS.setNodeCount(nodeCount);
        cmRS.setNumberOfThreads(numberOfThreads);
        cmRS.setQueueName(queueName);
        cmRS.setWallTimeLimit(wallTimeLimit);
        cmRS.setJobStartTime((int) jobstartTime);
        cmRS.setTotalPhysicalMemory(totalPhysicalMemory);
        cmRS.setComputationalProjectAccount(projectAccount);
        return cmRS;
    }

    // save uploaded file to new location
    private void saveToFile(InputStream uploadedInputStream,
                            String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }


}
