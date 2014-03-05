package org.scigap.vanillagateway.services;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.airavata.model.workspace.experiment.Experiment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;

@Path("/newjob")
public class NewJobHandler {
    private String name = "not initialized", description = "not initialized";

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String createJob(@FormDataParam("file") InputStream uploadedInputStream,
                            @FormDataParam("file") FormDataContentDisposition fileDetail,
                            @FormDataParam("name") final String name,
                            @FormDataParam("description") final String description) {

        Experiment experiment = new Experiment();
        experiment.setName(name);
        experiment.setDescription(description);


        //downloading the files to the server
        String uploadedFileLocation = "/Users/swithana/temp/" + fileDetail.getFileName();
        saveToFile(uploadedInputStream, uploadedFileLocation);

        return "Job Created Successfully" + " \nExperiment Name: "+ name+"\nDescription: "+description;
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
