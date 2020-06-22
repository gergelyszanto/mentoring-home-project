package com.mentoring.database;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbUpdates {

    private static final int CACHE_UPDATE_DELAY = 2000;

    private static final String UPDATE_LAST_ACCESS_VALUE_BY_EMAIL = "UPDATE access_token SET last_access = ? " +
            "WHERE user_id = (SELECT user_id FROM user WHERE email = ?)";
    private static final String UPDATE_PRODUCT_END_TIME_BY_FACTORY_ID = "UPDATE product SET end_time = ? " +
            "WHERE factory_id = ?";

    @Step("Updating production queue end time to be finished in the database.")
    public static void setProductionEndTimeByFactoryId(Database connection, String factoryId, String value) {
        connection.runUpdateLongColumnValue(
                UPDATE_PRODUCT_END_TIME_BY_FACTORY_ID,
                connection.getSingleStringMapper(2, factoryId),
                connection.getSingleStringMapper(1, value)
        );
    }

    @Step("Updating user's access token to be expired in the database.")
    public static void setLastAccessValueByEmailAddress(Database connection, String email, double value) {
        connection.runUpdateLongColumnValue(
                UPDATE_LAST_ACCESS_VALUE_BY_EMAIL,
                connection.getSingleStringMapper(2, email),
                connection.getSingleDoubleMapper(1, value)
        );
        waitForCacheUpdate();
    }

    private static void waitForCacheUpdate() {
        try {
            log.info("Waiting {}ms for cache to be updated...", CACHE_UPDATE_DELAY);
            Thread.sleep(CACHE_UPDATE_DELAY);
        } catch (InterruptedException e)  {
            log.error("Waiting failed", e);
        }
    }
}
