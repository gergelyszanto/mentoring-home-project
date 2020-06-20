package com.mentoring.database;

import com.mysql.cj.xdevapi.Table;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbUpdates {

    private static final int CACHE_UPDATE_DELAY = 2000;

    private static final String UPDATE_LAST_ACCESS_VALUE_BY_EMAIL = "UPDATE access_token SET last_access = ? " +
            "WHERE user_id = (SELECT user_id FROM user WHERE email = ?)";
    private static final String UPDATE_END_TIME_BY_FACTORY_ID = "UPDATE product SET end_time = ? " +
            "WHERE factory_id = ?";

    @Step("Updating user's access token to be expired in the database.")
    public static void setLastAccessValueByEmailAddress(Database connection, String email, double value) {
        connection.runUpdateLongColumnValue(
                UPDATE_LAST_ACCESS_VALUE_BY_EMAIL,
                connection.getSingleStringMapper(2, email),
                connection.getSingleDoubleMapper(1, value),
                TableColumn.ACCESS_TOKEN__LAST_ACCESS
        );
        waitForCacheUpdate();
    }

/*    @Step("Updating production queue end time to be finished in the database.")
    public static void setEndTimeByFactoryId(Database connection, String factoryId) {
        connection.runUpdateLongColumnValue(
                UPDATE_END_TIME_BY_FACTORY_ID,
                connection.getSingleStringMapper(1, factoryId),
                TableColumn.FACTORY__FACTORY_ID
        );
    }
*/
    private static void waitForCacheUpdate() {
        try {
            log.info("Waiting {}ms for cache to be updated...", CACHE_UPDATE_DELAY);
            Thread.sleep(CACHE_UPDATE_DELAY);
        } catch (InterruptedException e)  {
            log.error("Waiting failed", e);
        }
    }
}
