package org.anhcraft.spaciouslib.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    protected Connection conn;
    protected Statement state;

    /**
     * Disconnects from current connection
     * @throws SQLException
     */
    public void disconnect() throws SQLException {
        if(state != null){
            state.close();
            state = null;
        }
        if(conn != null){
            conn.close();
            conn = null;
        }
    }

    /**
     * Executes the given SQL statement (for statement that doesn't contain result such as INSERT, UPDATE, DELETE, DROP)
     * @param sql an SQL statement
     * @throws SQLException
     */
    public int update(String sql) throws SQLException {
        if(state == null){
            return 0;
        }
        return state.executeUpdate(sql);
    }

    /**
     * Executes the given SQL statement (for statement that contains result such as SELECT)
     * @param sql an SQL statement
     * @return result
     * @throws SQLException
     */
    public ResultSet query(String sql) throws SQLException {
        if(state == null){
            return null;
        }
        return state.executeQuery(sql);
    }

    /**
     * Gets the connection
     * @return the connection
     */
    public Connection getConnection(){
        return conn;
    }

    /**
     * Gets the statement of current connection
     * @return the statement
     */
    public Statement getStatement(){
        return state;
    }
}