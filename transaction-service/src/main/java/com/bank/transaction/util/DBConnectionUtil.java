package com.bank.transaction.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/bank_db";

    private static final String USERNAME = "postgres";

    private static final String PASSWORD =
            "root123";

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }
}