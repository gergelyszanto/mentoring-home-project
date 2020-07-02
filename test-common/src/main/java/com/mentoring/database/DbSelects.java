package com.mentoring.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DbSelects {

    private static final String USER_ID_BY_EMAIL =
            "SELECT user_id FROM user " +
                    "WHERE email = ?";
    private static final String USER_NAME_BY_EMAIL =
            "SELECT user_name FROM credentials c " +
                    "JOIN user u ON u.user_id = c.user_id " +
                    "WHERE u.email = ?";
    private static final String ACCESS_TOKEN_ID_BY_EMAIL =
            "SELECT access_token_id FROM access_token at " +
                    "JOIN user u ON at.user_id = u.user_id " +
                    "WHERE u.email = ?";
    private static final String CHARACTER_ID_BY_CHARACTER_NAME =
            "SELECT character_id FROM skyxp_character " +
                    "WHERE character_name = ?";
    private static final String CHARACTER_ID_BY_CHARACTER_NAME_AND_USER_ID =
            "SELECT character_id FROM skyxp_character " +
                    "WHERE character_name = ? AND user_id = ?";
    private static final String FACTORY_ID_BY_CHARACTER_ID =
            "SELECT factory_id FROM factory " +
                    "WHERE character_id = ?";

    public static String getUserIdByEmailAddress(Database connection, String email) {
        String result = connection.runSelectStringValue(
                USER_ID_BY_EMAIL,
                connection.getSingleStringMapper(1, email),
                TableColumn.USER__USER_ID
        );
        log.info("Query = {};\tparam = {};\tresult = {}", USER_ID_BY_EMAIL, email, result);
        return result;
    }

    public static String getUserNameByEmailAddress(Database connection, String email) {
        String result = connection.runSelectStringValue(
                USER_NAME_BY_EMAIL,
                connection.getSingleStringMapper(1, email),
                TableColumn.CREDENTIALS__USER_NAME
        );
        log.info("Query = {};\tparam = {};\tresult = {}", USER_NAME_BY_EMAIL, email, result);
        return result;
    }

    public static String getAccessTokenIdByEmailAddress(Database connection, String email) {
        String result = connection.runSelectStringValue(
                ACCESS_TOKEN_ID_BY_EMAIL,
                connection.getSingleStringMapper(1, email),
                TableColumn.ACCESS_TOKEN__ACCESS_TOKEN_ID
        );
        log.info("Query = {};\tparam = {};\tresult = {}", ACCESS_TOKEN_ID_BY_EMAIL, email, result);
        return result;
    }

    public static String getCharacterIdByCharacterNameAndUserId(Database connection, String characterName, String userId) {
        String result = connection.runSelectStringValue(
                CHARACTER_ID_BY_CHARACTER_NAME_AND_USER_ID,
                connection.getSingleStringMapper(1, characterName),
                connection.getSingleStringMapper(2, userId),
                TableColumn.SKYXP_CHARACTER__CHARACTER_ID
        );
        log.info("Query = {};\tparam1 = {};\tparam2 = {}\tresult = {}",
                CHARACTER_ID_BY_CHARACTER_NAME_AND_USER_ID, characterName, userId, result);
        return result;
    }

    public static String getFactoryIdByCharacterId(Database connection, String characterId) {
        String result = connection.runSelectStringValue(
                FACTORY_ID_BY_CHARACTER_ID,
                connection.getSingleStringMapper(1, characterId),
                TableColumn.FACTORY__FACTORY_ID
        );
        log.info("Query = {};\tparam = {}\t result = {}", FACTORY_ID_BY_CHARACTER_ID, characterId, result);
        return result;
    }

    public static String getFactoryId(Database database, String email, String characterName) {
        String userId = getUserIdByEmailAddress(database, email);
        String characterId = getCharacterIdByCharacterNameAndUserId(database, characterName, userId);
        String factoryId = getFactoryIdByCharacterId(database, characterId);
        log.info("Getting Factory ID: {} by User ID: {} and Character ID: {}", factoryId, userId, characterId);
        return factoryId;
    }

}
