package org.scigap.cybergateway.airavata;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.api.client.AiravataClientFactory;
import org.apache.airavata.api.error.AiravataClientException;
import org.apache.airavata.api.error.AiravataSystemException;
import org.apache.airavata.api.error.ExperimentNotFoundException;
import org.apache.airavata.api.error.InvalidRequestException;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.JobStatus;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by swithana on 3/3/14.
 */
public class AiravataClient {
    private final static Logger logger = LoggerFactory.getLogger(AiravataClient.class);

    private Airavata.Client client;
    private String thriftServerHost;
    private int thriftServerPort;

    private static AiravataClient instance = null;

    protected AiravataClient() {
        loadConfigurations();
        this.client = AiravataClientFactory.createAiravataClient(thriftServerHost,thriftServerPort);
    }

    public static AiravataClient getInstance() {

        return new AiravataClient();
    }

    public String submitJob(Experiment experiment){
        try {
            String expID = client.createExperiment(experiment);
            launchExperiment(expID);

            return expID;
        } catch (InvalidRequestException e) {
e.printStackTrace();        } catch (AiravataClientException e) {
            e.printStackTrace();
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        }catch (TException e) {
            e.printStackTrace();
        }

        return "exception occured in creating experiment";
    }

    private void launchExperiment (String expId)
            throws TException{
        try {
            client.launchExperiment(expId, "testToken");

        } catch (ExperimentNotFoundException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new ExperimentNotFoundException(e);
        } catch (AiravataSystemException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new AiravataSystemException(e);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new InvalidRequestException(e);
        } catch (AiravataClientException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new AiravataClientException(e);
        }catch (TException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new TException(e);
        }
    }

    public Map<String, JobStatus> getJobStatuses(String expId)
            {
        try {
            return client.getJobStatuses(expId);

        } catch (ExperimentNotFoundException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
        } catch (AiravataSystemException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
        } catch (InvalidRequestException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
        } catch (AiravataClientException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
        }catch (TException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
        }
            return null;
    }
    public Airavata.Client getClient() {
        return client;
    }

    public void setClient(Airavata.Client client) {
        this.client = client;
    }


    public List<Experiment> getAllExperiments(String userName){
        try {
            return client.getAllUserExperiments(userName);
        } catch (InvalidRequestException e) {
e.printStackTrace();        } catch (AiravataClientException e) {
            e.printStackTrace();
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        }catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Experiment getExperiment(String experimentID) {
        try {
            return client.getExperiment(experimentID);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while getting the experiment..."+ experimentID+"  ", e.getMessage());
        } catch (ExperimentNotFoundException e) {
            logger.error("Error occured while getting the experiment..."+ experimentID+"  ", e.getMessage());
        } catch (AiravataClientException e) {
            logger.error("Error occured while getting the experiment..."+ experimentID+"  ", e.getMessage());
        } catch (AiravataSystemException e) {
            logger.error("Error occured while getting the experiment..."+ experimentID+"  ", e.getMessage());
        }catch (TException e) {
            logger.error("Error occured while getting all the experiments...", e.getMessage());
        }
        return null;
    }

    public void cancelExperiment(String experimentID) {
        //fixme log the exceptions
        try {
            client.terminateExperiment(experimentID);
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (ExperimentNotFoundException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        }catch (TException e) {
            logger.error("Error occured while terminating the experiment...", e.getMessage());
        }
    }

    private void loadConfigurations() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("airavata-client.properties");

        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Error reading the client.properties file",e.getMessage());
        }

        this.thriftServerHost = properties.getProperty("airavata.server.url");
        this.thriftServerPort = Integer.parseInt(properties.getProperty("airavata.server.port"));

        logger.info("Airavata Server host: " + thriftServerHost);
        logger.info("Airavata Server Port: " + thriftServerPort);
    }
}
