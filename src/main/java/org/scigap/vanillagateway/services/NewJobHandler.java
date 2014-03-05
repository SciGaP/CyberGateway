package org.scigap.vanillagateway.services;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.airavata.model.workspace.experiment.DataObjectType;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.scigap.vanillagateway.airavata.AiravataClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
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
                            @FormDataParam("description") final String description) {

        Experiment experiment = createExperiment("test", "admin", name, description, "test", null);
        String experimentId = submitJob(experiment);


        //downloading the files to the server
        String uploadedFileLocation = "/Users/swithana/temp/" + fileDetail.getFileName();
        saveToFile(uploadedInputStream, uploadedFileLocation);

        return "Job Created Successfully" + " \nExperiment Name: " + name + "\nDescription: " + description+
                "\nExperimentID = "+experimentId;
    }
    private String submitJob(Experiment experiment){
        if(client == null){
            client = new AiravataClient();
        }
        return client.submitJob(experiment);
    }
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
        experiment.setExperimentInputs(experimentInputList);
        return experiment;
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
