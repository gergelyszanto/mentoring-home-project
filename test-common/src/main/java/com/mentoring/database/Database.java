package com.mentoring.database;

import com.mentoring.config.Config;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

@Slf4j
public class Database {

    @Getter
    private final Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(Config.getJBDC(), Config.getDbUser(), Config.getDbPassword());
        } catch (SQLException e) {
            log.error("Cannot connect to Database.", e);
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

    void runUpdateLongColumnValue(String queryString, Consumer<PreparedStatement> stringMapper, Consumer<PreparedStatement> intMapper, TableColumn resultColumn) {
        try {
            PreparedStatement statement = connection.prepareStatement(queryString);
            stringMapper.accept(statement);
            intMapper.accept(statement);
            statement.executeUpdate();
            statement.close();
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

    Consumer<PreparedStatement> getSingleDoubleMapper(int paramIndex, double value){
        return statement -> {
            try {
                statement.setDouble(paramIndex, value);
            } catch (SQLException e) {
                throw new IllegalArgumentException("Cannot map statement parameter " + paramIndex + " with value " + value, e);
            }
        };
    }
}