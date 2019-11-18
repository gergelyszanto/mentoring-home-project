package com.mentoring.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DbSelects {

    private static final String USER_ID_BY_EMAIL = "SELECT user_id FROM user WHERE email = ?";
    private static final String USER_NAME_BY_EMAIL = "SELECT user_name FROM credentials c " +
            "JOIN user u ON u.user_id = c.user_id " +
            "WHERE u.email = ?";
    private static final String ACCESS_TOKEN_ID_BY_EMAIL = "SELECT access_token_id FROM access_token at " +
            "JOIN user u ON at.user_id = u.user_id " +
            "WHERE u.email = ?";
    private static final String CHARACTER_ID_BY_CHARACTER_NAME = "SELECT character_id FROM skyxp_character " +
            "WHERE character_name = ?";

    public static String getUserIdByEmailAddress(Database connection, String email) throws SQLException {
        String result = connection.runSelectStringValue(USER_ID_BY_EMAIL, connection.getSingleStringMapper(1, email), TableColumn.USER__USER_ID);
        log.info("Query = {};\tparam = {};\tresult = {}", USER_ID_BY_EMAIL, email, result);
        return result;
    }

    public static String getUserNameByEmailAddress(Database connection, String email) throws SQLException {
        String result = connection.runSelectStringValue(USER_NAME_BY_EMAIL, connection.getSingleStringMapper(1, email), TableColumn.CREDENTIALS__USER_NAME);
        log.info("Query = {};\tparam = {};\tresult = {}", USER_NAME_BY_EMAIL, email, result);
        return result;
    }

    public static String getAccessTokenIdByEmailAddress(Database connection, String email) throws SQLException {
        String result = connection.runSelectStringValue(ACCESS_TOKEN_ID_BY_EMAIL, connection.getSingleStringMapper(1, email), TableColumn.ACCESS_TOKEN__ACCESS_TOKEN_ID);
        log.info("Query = {};\tparam = {};\tresult = {}", ACCESS_TOKEN_ID_BY_EMAIL, email, result);
        return result;
    }

    public static String getCharacterIdByCharacterName(Database connection, String characterName) throws SQLException {
        String result = connection.runSelectStringValue(CHARACTER_ID_BY_CHARACTER_NAME, connection.getSingleStringMapper(1, characterName), TableColumn.SKYXP_CHARACTER__CHARACTER_ID);
        log.info("Query = {};\tparam = {};\tresult = {}", CHARACTER_ID_BY_CHARACTER_NAME, characterName, result);
        return result;
    }
}
