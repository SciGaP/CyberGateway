package org.scigap.vanillagateway.airavata;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.api.error.AiravataClientException;
import org.apache.airavata.api.error.AiravataSystemException;
import org.apache.airavata.api.error.ExperimentNotFoundException;
import org.apache.airavata.api.error.InvalidRequestException;
import org.apache.airavata.model.workspace.experiment.DataObjectType;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swithana on 3/3/14.
 */
public class AiravataClientTester {
    private final static Logger logger = LoggerFactory.getLogger(AiravataClient.class);

    private AiravataClient airavataClient;
    private Airavata.Client client;

    public Experiment createSimpleExperiment(String projectID,
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

    @BeforeTest
    public void setup() {
        airavataClient = AiravataClient.getInstance();
        client = airavataClient.getClient();
    }

    @Test
    public void testClient() {
        Experiment simple_experiment = createSimpleExperiment("project1", "admin", "testExperiment", "testing the vanilla gateway"
                , "testapp1", new ArrayList<DataObjectType>());
        try {
            String exp_id = client.createExperiment(simple_experiment);
            logger.info("Created Experiment with the ID: " + exp_id);


            Experiment resultingExperiment = client.getExperiment(exp_id);
            logger.info("getExperiment result :" + resultingExperiment.getProjectID());

        } catch (InvalidRequestException e) {
            logger.error("Error Occurred at client Testing", e.getMessage());
        } catch (AiravataClientException e) {
            logger.error("Error Occurred at client Testing", e.getMessage());
        } catch (AiravataSystemException e) {
            logger.error("Error Occurred at client Testing", e.getMessage());
        } catch (ExperimentNotFoundException e) {
            logger.error("Error Occurred at client Testing", e.getMessage());
        } catch (TException e) {
            logger.error("Error Occurred at client Testing", e.getMessage());
        }
    }

    @Test
    public void testGetExperiments() throws InterruptedException {


        List<Experiment> experiments = airavataClient.getAllExperiments("admin");
        Thread.sleep(4000);
        logger.info(experiments.get(0).getExperimentID());

    }

}
