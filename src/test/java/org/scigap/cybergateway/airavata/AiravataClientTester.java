package org.scigap.cybergateway.airavata;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.api.error.AiravataClientException;
import org.apache.airavata.api.error.AiravataSystemException;
import org.apache.airavata.api.error.ExperimentNotFoundException;
import org.apache.airavata.api.error.InvalidRequestException;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.apache.airavata.model.workspace.experiment.ComputationalResourceScheduling;
import org.apache.airavata.model.workspace.experiment.DataObjectType;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.UserConfigurationData;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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

        for (Experiment experiment : experiments) {
            System.out.println(experiment.getExperimentID()+ "\t"+experiment.getName() );
        }
        logger.info(experiments.get(0).getExperimentID());

    }

    @Test
    public void testCreateExperiment() {
        Experiment experiment = createExperiment("cybergateway", "admin", "tester-sachith", "testing vanilla gateway create experiment"
                , "SimpleEcho3", null);
        String experimentId = submitJob(experiment);
        System.out.println(experimentId);
        Assert.assertNotNull(experimentId);
    }

    private String submitJob(Experiment experiment) {

        airavataClient = AiravataClient.getInstance();

        return airavataClient.submitJob(experiment);
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
                ExperimentModelUtil.createComputationResourceScheduling("stampede.tacc.xsede.org", 1, 1, 1, "normal", 0, 0, 1, "TG-STA110014S");
        scheduling.setResourceHostId("stampede-host");
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
}
