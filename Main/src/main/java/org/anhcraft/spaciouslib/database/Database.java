package org.anhcraft.spaciouslib.database;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

public abstract class Database {
    public enum TaskStatus {
        CONTINUE,
        STOP
    }

    protected Connection conn;
    protected Statement state;
    private LinkedHashMap<String, BukkitTask> tasks = new LinkedHashMap<>();

    /**
     * Disconnects from current connection
     * @throws SQLException
     */
    public void disconnect() throws SQLException {
        for(String s : tasks.keySet()){
            stopTask(s);
        }
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
     * Creates a new repeat task which execute the given SQL statement (for statement that contains result such as SELECT)
     * @param id task's id
     * @param sql an SQL statement
     * @param intervalTimeSeconds interval time between executes (seconds)
     * @param taskResult result which you want to choose for that task
     */
    public void createQueryTask(String id, String sql, long intervalTimeSeconds, QueryTaskResult taskResult){
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    TaskStatus s = taskResult.result(query(sql));
                    if(s.equals(TaskStatus.STOP)){
                        stopTask(id);
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(SpaciousLib.instance, 0, intervalTimeSeconds * 20);
        tasks.put(id, task);
    }

    /**
     * Creates a new repeat task which execute the given SQL statement (for statement that doesn't contain result such as INSERT, UPDATE, DELETE, DROP)
     * @param id task's id
     * @param sql an SQL statement
     * @param intervalTimeSeconds interval time between executes (seconds)
     * @param taskResult result which you want to choose for that task
     */
    public void createUpdateTask(String id, String sql, long intervalTimeSeconds, UpdateTaskResult taskResult){
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    TaskStatus s = taskResult.result(update(sql));
                    if(s.equals(TaskStatus.STOP)){
                        stopTask(id);
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(SpaciousLib.instance, 0, intervalTimeSeconds * 20);
        tasks.put(id, task);
    }

    /**
     * Stops a specific task immediately
     * @param id task's id
     */
    public void stopTask(String id){
        if(tasks.containsKey(id)){
            tasks.get(id).cancel();
            tasks.remove(id);
        }
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