package org.scigap.vanillagateway.airavata;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.api.client.AiravataClientFactory;
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

    private void loadConfigurations() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("airavata-client.properties");

        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.thriftServerHost = properties.getProperty("airavata.server.url");
        this.thriftServerPort = Integer.parseInt(properties.getProperty("airavata.server.port"));

        logger.info("Airavata Server host: " + thriftServerHost);
        logger.info("Airavata Server Port: " + thriftServerPort);
    }
}
