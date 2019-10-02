package com.mentoring.utilities;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public final class ConfigUtils {

    private static final String SKY_XPLORE_LOCALHOST = "skyxplore-localhost";
    private static final String SKY_XPLORE_PROD = "skyxplore-prod";
    private static final String PATH_WEB_EXTENSION = "/web";

    private static final String ENVIRONMENT = "ENVIRONMENT";
    private static String[] validEnvironmentConfigurationNames = {SKY_XPLORE_LOCALHOST, SKY_XPLORE_PROD};

    private ConfigUtils() {
    }

    public static Properties loadProperties() {
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

    public static boolean isLocalEnvironmentUsed() {
        return System.getenv(ConfigUtils.ENVIRONMENT).equals(ConfigUtils.SKY_XPLORE_LOCALHOST);
    }
}