package com.mentoring.framework;

import com.mentoring.model.Browser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public final class Config {

    public static final int LOAD_WAIT;
    public static final String APPLICATION_URL;
    static final Browser BROWSER;

    public static final String SKY_XPLORE_LOCALHOST = "skyxplore-localhost";
    private static final String SKY_XPLORE_REMOTE = "skyxplore-remote";

    private static final String BASE_URL;
    private static final String PORT;
    private static final String ENVIRONMENT = "ENVIRONMENT";
    private static String[] validEnvironmentConfigurationNames = {SKY_XPLORE_LOCALHOST, SKY_XPLORE_REMOTE};

    static {
        Properties prop = loadProperties();
        LOAD_WAIT = Integer.parseInt(prop.getProperty("load_wait"));
        BROWSER = Browser.parse(System.getenv("BROWSER"));
        PORT = prop.getProperty("port");
        if (System.getenv(ENVIRONMENT).equalsIgnoreCase(SKY_XPLORE_REMOTE)) {
            //TODO: Replace static IP address with dynamic get from emails
            BASE_URL = "http://".concat("62.165.192.145");
        } else {
            BASE_URL = prop.getProperty("base_url");
        }
        APPLICATION_URL = BASE_URL.concat(":").concat(PORT);

    }

    private Config() {
    }

    private static Properties loadProperties() {
        String testEnvironmentName = System.getenv(ENVIRONMENT).toLowerCase();
        String propertiesFileName = validateEnvironment(testEnvironmentName) + "-config.properties";
        return loadProperties(propertiesFileName);
    }

    private static Properties loadProperties(String propertyFile) {
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile)));
        } catch (IOException e) {
            log.error("Error loading the " + propertyFile + " file", e);
        }
        return properties;
    }

    private static String validateEnvironment(String currentEnvironment) {
        if (currentEnvironment == null) {
            log.error("Environment is not set. Please choose from: {}", Arrays.toString(validEnvironmentConfigurationNames));
            System.exit(1);
        }
        return compareCurrentEnvironmentWithValidEnvironments(currentEnvironment);
    }

    private static String compareCurrentEnvironmentWithValidEnvironments(String currentEnvironment) {
        String validEnvironmentName = "";
        for (int i = 0; i < validEnvironmentConfigurationNames.length; i++) {
            if (validEnvironmentConfigurationNames[i].equalsIgnoreCase(currentEnvironment)) {
                validEnvironmentName = validEnvironmentConfigurationNames[i];
                break;
            } else if (i == validEnvironmentConfigurationNames.length - 1) {
                log.error("Failed loading environment configuration. Given environment name '{}' is not valid."
                        + " Please choose from: {}", currentEnvironment, Arrays.toString(validEnvironmentConfigurationNames));
                System.exit(1);
            }
        }
        return validEnvironmentName;
    }
}