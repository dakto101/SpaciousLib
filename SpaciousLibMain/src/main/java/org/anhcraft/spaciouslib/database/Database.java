package org.anhcraft.spaciouslib.database;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents a database implementation.
 */
public abstract class Database {
    protected Connection conn;
    protected Statement state;

    /**
     * Disconnects from this database
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
     * Executes the given SQL statement.<br>
     * Only for statement that doesn't contain result such as INSERT, UPDATE, DELETE, DROP
     * @param sql an SQL statement
     */
    public int update(String sql) throws SQLException {
        if(state == null){
            return 0;
        }
        return state.executeUpdate(sql);
    }

    /**
     * Executes the given SQL statement.<br>
     * Only for statement that contains result such as SELECT
     * @param sql an SQL statement
     * @return the result after executes that statement
     */
    public ResultSet query(String sql) throws SQLException {
        if(state == null){
            return null;
        }
        return state.executeQuery(sql);
    }

    /**
     * Gets the connection
     * @return the connection object
     */
    public Connection getConnection(){
        return conn;
    }

    /**
     * Gets the statement
     * @return the statement object
     */
    public Statement getStatement(){
        return state;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Database db = (Database) o;
            return new EqualsBuilder().append(db.conn, this.conn).append(db.state, this.state).build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(5, 31)
                .append(conn).append(state).build();
    }
}