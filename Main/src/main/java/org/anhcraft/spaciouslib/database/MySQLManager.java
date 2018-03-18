package org.anhcraft.spaciouslib.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLManager extends Database{
    /**
     * Creates a new MySQL connection
     * @param host hostname
     * @param port port
     * @param db database
     * @param useSSL true if you want to use SSL for this connection
     * @param user username
     * @param pass password
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void connect(String host, int port, String db, boolean useSSL, String user, String pass) throws SQLException, ClassNotFoundException {
        if(conn == null && state == null){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL="+useSSL, user, pass);
            state = conn.createStatement();
        }
    }
}
