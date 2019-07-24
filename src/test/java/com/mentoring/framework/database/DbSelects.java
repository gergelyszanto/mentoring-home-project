package com.mentoring.framework.database;

import java.sql.SQLException;

public class DbSelects {

    private static final String USER_ID_BY_EMAIL = "SELECT user_id FROM user WHERE email = ?";
    private static final String USER_NAME_BY_EMAIL = "SELECT user_name FROM credentials c " +
            "JOIN user u ON u.user_id = c.user_id " +
            "WHERE u.email = ?";
    private static final String ACCESS_TOKEN_ID_BY_EMAIL = "SELECT access_token_id FROM access_token at " +
            "JOIN user u ON at.user_id = u.user_id " +
            "WHERE u.email = ?";

    public static String getUserIdByEmailAddress(Database connection, String email) throws SQLException {
        return connection.runSelectStringValue(USER_ID_BY_EMAIL, connection.getSingleStringMapper(1, email), TableColumn.USER__USER_ID);
    }

    public static String getUserNameByEmailAddress(Database connection, String email) throws SQLException {
        return connection.runSelectStringValue(USER_NAME_BY_EMAIL, connection.getSingleStringMapper(1, email), TableColumn.CREDENTIALS__USER_NAME);
    }

    public static String getAccessTokenIdByEmailAddress(Database connection, String email) throws SQLException {
        return connection.runSelectStringValue(ACCESS_TOKEN_ID_BY_EMAIL, connection.getSingleStringMapper(1, email), TableColumn.ACCESS_TOKEN__ACCESS_TOKEN_ID);
    }
}
