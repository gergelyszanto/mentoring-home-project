package com.mentoring.config;

import com.mentoring.utilities.EmailUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public final class Config {

    public static final int LOAD_WAIT;
    private static final String APPLICATION_URL;
    public static final Browser BROWSER;
    private static final String SKY_XPLORE_LOCALHOST = "skyxplore-localhost";
    private static final String SKY_XPLORE_PROD = "skyxplore-prod";
    private static final String PATH_WEB_EXTENSION = "/web";

    private static final String BASE_URL;
    private static final String PORT;
    private static final String ENVIRONMENT = "ENVIRONMENT";
    private static String[] validEnvironmentConfigurationNames = {SKY_XPLORE_LOCALHOST, SKY_XPLORE_PROD};
    private static final String DATABASE_NAME;
    private static final String JDBC;
    private static final String DB_USER;
    private static final String DB_PASSWORD;


    static {
        Properties prop = loadProperties();
        LOAD_WAIT = Integer.parseInt(prop.getProperty("load_wait"));
        BROWSER = Browser.parse(System.getenv("BROWSER"));
        PORT = prop.getProperty("port");
        if (System.getenv(ENVIRONMENT).equalsIgnoreCase(SKY_XPLORE_PROD)) {
            BASE_URL = EmailUtils.getIpAddressFromEmail();
        } else {
            BASE_URL = prop.getProperty("base_url");
        }
        APPLICATION_URL = BASE_URL.concat(":").concat(PORT);
        DATABASE_NAME = prop.getProperty("database_name");
        if (System.getenv(ENVIRONMENT).equalsIgnoreCase(SKY_XPLORE_LOCALHOST)) {
            JDBC = "jdbc:mysql://localhost/" + DATABASE_NAME + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        } else {
            // What should we do on prod? Do we have rights to connect to Prod database?
            JDBC = "";
        }
        DB_USER = prop.getProperty("db_user");
        DB_PASSWORD = prop.getProperty("db_password");
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

    public static boolean isLocalEnvironmentUsed() {
        return System.getenv(Config.ENVIRONMENT).equals(Config.SKY_XPLORE_LOCALHOST);
    }

    public static String getApplicationUrl() {
        return isLocalEnvironmentUsed() ? APPLICATION_URL + PATH_WEB_EXTENSION : APPLICATION_URL;
    }

    public static String getBaseUrl() {
        return APPLICATION_URL;
    }

    public static String getJBDC() {
        return JDBC;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}