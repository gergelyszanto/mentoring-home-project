package com.mentoring;

import com.mentoring.framework.utils.Database;
import com.mentoring.framework.utils.DbSelects;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

@Slf4j
public class DbPocTest {

    private Database database;

    @BeforeClass(alwaysRun = true)
    private void setup() {
        database = new Database();
    }

    @AfterClass(alwaysRun = true)
    private void close() {
        database.disconnectFromDb();
    }

    @Test
    public void testUserIdByEmail() throws SQLException {
        String userID = DbSelects.getUserIdByEmailAddress(database, "tesztfix@tesztfix.com");
        log.info("User ID is: {}", userID);
    }

    @Test
    public void testUserNameByEmail() throws SQLException {
        String userName = DbSelects.getUserNameByEmailAddress(database, "tesztfix@tesztfix.com");
        log.info("User name is: {}", userName);
    }

    @Test
    public void testAccessTokenIdByEmail() throws SQLException {
        String accessTokenId = DbSelects.getAccessTokenIdByEmailAddress(database, "tesztfix@tesztfix.com");
        log.info("Access token id is: {}", accessTokenId);
    }
}