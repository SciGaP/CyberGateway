package org.scigap.vanillagateway.airavata;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.api.client.AiravataClientFactory;
import org.apache.airavata.api.error.AiravataClientException;
import org.apache.airavata.api.error.AiravataSystemException;
import org.apache.airavata.api.error.InvalidRequestException;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by swithana on 3/3/14.
 */
public class AiravataClient {
    private final static Logger logger = LoggerFactory.getLogger(AiravataClient.class);

    private Airavata.Client client;
    private String thriftServerHost;
    private int thriftServerPort;

    public AiravataClient() {
        loadConfigurations();
        this.client = AiravataClientFactory.createAiravataClient(thriftServerHost,thriftServerPort);
    }
    public String submitJob(Experiment experiment){
        try {
            String expID = client.createExperiment(experiment);
            return expID;
        } catch (InvalidRequestException e) {
            logger.error("Error Creating the Experiment: "+e.getMessage());
        } catch (AiravataClientException e) {
            logger.error("Error Creating the Experiment: " + e.getMessage());
        } catch (AiravataSystemException e) {
            logger.error("Error Creating the Experiment: "+e.getMessage());
        }catch (TException e) {
            logger.error("Error Creating the Experiment: "+e.getMessage());
        }
        return null;
    }
    public Airavata.Client getClient() {
        return client;
    }

    public void setClient(Airavata.Client client) {
        this.client = client;
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
