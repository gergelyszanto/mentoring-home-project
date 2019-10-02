package com.mentoring.framework;

import com.mentoring.framework.utils.EmailUtils;
import com.mentoring.model.Browser;
import com.mentoring.utilities.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public final class Config {

    public static final int LOAD_WAIT;
    private static final String APPLICATION_URL;
    static final Browser BROWSER;
    private static final String SKY_XPLORE_LOCALHOST = "skyxplore-localhost";
    private static final String SKY_XPLORE_PROD = "skyxplore-prod";
    private static final String PATH_WEB_EXTENSION = "/web";
    private static final String BASE_URL;
    private static final String PORT;
    private static final String ENVIRONMENT = "ENVIRONMENT";
    private static final String DATABASE_NAME;
    private static final String JDBC;
    private static final String DB_USER;
    private static final String DB_PASSWORD;


    static {
        Properties prop = ConfigUtils.loadProperties();
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

    public static boolean isLocalEnvironmentUsed() {
        return System.getenv(Config.ENVIRONMENT).equals(Config.SKY_XPLORE_LOCALHOST);
    }

    public static String getApplicationUrl() {
        return isLocalEnvironmentUsed()? APPLICATION_URL + PATH_WEB_EXTENSION : APPLICATION_URL;
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