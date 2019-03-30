package com.mentoring.framework;

import com.mentoring.model.Browser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class Config {

    public static final int LOAD_WAIT;
    public static final String BASE_URL = "http://localhost:8080";
    static final Browser BROWSER;
    private static final String CONFIG_PROPERTIES = "config.properties";

    static {
        Properties prop = loadProperties(CONFIG_PROPERTIES);
        LOAD_WAIT = Integer.parseInt(prop.getProperty("load_wait"));
        BROWSER = Browser.parse(System.getenv("BROWSER"));
    }

    private static Properties loadProperties(String propertyFile) {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile));
        } catch (IOException e) {
            log.error("Error loading the " + propertyFile + " file", e);
        }
        return properties;
    }
}
