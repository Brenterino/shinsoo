package dev.zygon.shinsoo.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {

    Connection getConnection() throws SQLException;

    void release(Connection connection) throws SQLException;
}
