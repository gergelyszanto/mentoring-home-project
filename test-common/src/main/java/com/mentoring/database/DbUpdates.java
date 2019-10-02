package com.mentoring.database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbUpdates {

    private static final int CACHE_UPDATE_DELAY = 2000;

    private static final String UPDATE_LAST_ACCESS_VALUE_BY_EMAIL = "UPDATE access_token SET last_access = ?" +
            " WHERE user_id = (SELECT user_id FROM user WHERE email = ?)";

//    @Step("Updating user's access token to be expired in the database.")
    public static void setLastAccessValueByEmailAddress(Database connection, String email, double value) {
        connection.runUpdateLongColumnValue(
                UPDATE_LAST_ACCESS_VALUE_BY_EMAIL,
                connection.getSingleStringMapper(2, email),
                connection.getSingleDoubleMapper(1, value),
                TableColumn.ACCESS_TOKEN__LAST_ACCESS
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
