package org.anhcraft.spaciouslib.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase extends Database{
    /**
     * Creates a new connection for a MySQL database
     * @param host the IP address or hostname of the MySQL server (the default is localhost or 127.0.0.1)
     * @param port the TCP/IP port which is listening by the MySQL server (the default is 3306)
     * @param database the name of the database
     * @param useSSL uses to enable SSL encryption
     * @param user the name of the user
     * @param pass the password of the user
     */
    public void connect(String host, int port, String database, boolean useSSL, String user, String pass) throws SQLException, ClassNotFoundException {
        if(conn == null && state == null){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL="+useSSL, user, pass);
            state = conn.createStatement();
        }
    }
}
