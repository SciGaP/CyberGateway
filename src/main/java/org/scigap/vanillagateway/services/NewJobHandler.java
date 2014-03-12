package org.scigap.vanillagateway.services;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.apache.airavata.model.workspace.experiment.ComputationalResourceScheduling;
import org.apache.airavata.model.workspace.experiment.DataObjectType;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.UserConfigurationData;
import org.scigap.vanillagateway.airavata.AiravataClient;

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
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String createJob(@FormDataParam("file") InputStream uploadedInputStream,
                            @FormDataParam("file") FormDataContentDisposition fileDetail,
                            @FormDataParam("name") final String name,
                            @FormDataParam("application") final String application,
                            @FormDataParam("description") final String description) {

        // for trestles
//        Experiment experiment = createExperiment("vanillagateway", "admin", name, description, "SimpleEcho2", null);

        //for stampede
        Experiment experiment = createExperiment("vanillagateway", "admin", name, description, "US3EchoStampede", null);
        String experimentId = submitJob(experiment);


        //downloading the files to the server
       // String uploadedFileLocation = "/Users/swithana/temp/" + fileDetail.getFileName();
        //saveToFile(uploadedInputStream, uploadedFileLocation);

//        return application;
        return "Job Created Successfully" + " \nExperiment Name: " + name + "\nDescription: " + description+
                "\nExperimentID = "+experimentId;
    }

    private String submitJob(Experiment experiment){
        if(client == null){
            client = AiravataClient.getInstance();
        }
        return client.submitJob(experiment);
    }

    // FIXME get the inputs and scheduling from the client
    private Experiment createExperiment(String projectID,
                                             String userName,
                                             String experimentName,
                                             String expDescription,
                                             String applicationId,
                                             List<DataObjectType> experimentInputList) {
        Experiment experiment = new Experiment();
        experiment.setProjectID(projectID);
        experiment.setUserName(userName);
        experiment.setName(experimentName);
        experiment.setDescription(expDescription);
        experiment.setApplicationId(applicationId);


        List<DataObjectType> exInputs = new ArrayList<DataObjectType>();
        DataObjectType input = new DataObjectType();
        input.setKey("echo_input");
        //input.setType(DataType.STRING.toString());
        input.setValue("echo_output=Hello World");
        exInputs.add(input);

        experiment.setExperimentInputs(exInputs);

        List<DataObjectType> exOut = new ArrayList<DataObjectType>();
        DataObjectType output = new DataObjectType();
        output.setKey("echo_output");
        //output.setType(DataType.STRING.toString());
        output.setValue("");
        exOut.add(output);

        experiment.setExperimentOutputs(exOut);

/*
        ComputationalResourceScheduling scheduling = createComputationResourceScheduling("trestles.sdsc.edu", 1, 1, 1,
                "normal", 0, 0, 1, "sds128");
        scheduling.setResourceHostId("gsissh-trestles");
*/
        //for stampede
        ComputationalResourceScheduling scheduling =
                ExperimentModelUtil.createComputationResourceScheduling("stampede.tacc.xsede.org", 1, 1, 1, "normal", 0, 0, 1, "TG-MCB070039N");
        scheduling.setResourceHostId("gsissh-stampede");
        UserConfigurationData userConfigurationData = new UserConfigurationData();
        userConfigurationData.setAiravataAutoSchedule(false);
        userConfigurationData.setOverrideManualScheduledParams(false);
        userConfigurationData.setComputationalResourceScheduling(scheduling);
        experiment.setUserConfigurationData(userConfigurationData);

        return experiment;
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
