package com.mentoring.framework.utils;

public enum TableColumn {
    ACCESS_TOKEN__ACCESS_TOKEN_ID("access_token_id"),
    ACCESS_TOKEN__CHARACTER_ID("character_id"),
    ACCESS_TOKEN__LAST_ACCESS("last_access"),
    ACCESS_TOKEN__USER_ID("user_id"),
    CREDENTIALS__USER_ID("user_id"),
    CREDENTIALS__PASSWORD("password"),
    CREDENTIALS__USER_NAME("user_name"),
    USER__USER_ID("user_id"),
    USER__EMAIL("email"),
    USER__ROLES("roles")
    ;

    private String column;

    TableColumn(String column) {
        this.column = column;
    }

    public String columnName() {
        return column;
    }
}
