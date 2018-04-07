package org.anhcraft.spaciouslib.database;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
     * Disconnects from current database
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
     * @return the result after executes that statement
     * @throws SQLException
     */
    public ResultSet query(String sql) throws SQLException {
        if(state == null){
            return null;
        }
        return state.executeQuery(sql);
    }

    /**
     * Gets the database connection
     * @return connection object
     */
    public Connection getConnection(){
        return conn;
    }

    /**
     * Gets the statement of current database connection
     * @return statement object
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
        return new HashCodeBuilder(4, 31)
                .append(conn).append(state).toHashCode();
    }
}