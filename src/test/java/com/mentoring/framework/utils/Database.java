package com.mentoring.framework.utils;

import com.mentoring.framework.Config;
import java.sql.*;
import java.util.function.Consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Database {

    @Getter
    private final Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(Config.getJBDC(), Config.getDbUser(), Config.getDbPassword());
        } catch (SQLException e) {
            log.error("Cannot connect to Database. ", e);
            throw new IllegalStateException("Cannot connect to Database.", e);
        }
    }

    public void disconnectFromDb() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Cannot disconnect from Database. ", e);
            throw new IllegalStateException("Cannot disconnect from Database.", e);
        }
    }

    String runSelectStringValue(String queryString, Consumer<PreparedStatement> paraMapper, TableColumn resultColumn) {
        try {
            String result = "";
            PreparedStatement statement = connection.prepareStatement(queryString);
            paraMapper.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString(resultColumn.columnName());
            }
            statement.close();
            return result;
        } catch (SQLException e) {
            log.error("Error running database query: " + queryString);
            throw new AssertionError(e);
        }
    }

    Consumer<PreparedStatement> getSingleStringMapper(int paramIndex, String value){
        return statement -> {
            try {
                statement.setString(paramIndex, value);
            } catch (SQLException e) {
                throw new IllegalArgumentException("Cannot map statement parameter " + paramIndex + " with value " + value, e);
            }
        };
    }
}