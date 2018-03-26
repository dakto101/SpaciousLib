package org.anhcraft.spaciouslib.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLManager extends Database{
    /**
     * Creates a new MySQL connection
     * @param hostname the IP address or hostname of the MYSQL server (the default is localhost or 127.0.0.1)
     * @param port the TCP/IP port on which the MySQL server is listening (the default is 3306)
     * @param database the name of the database
     * @param useSSL uses to enable SSL encryption
     * @param user the username of the user
     * @param pass the password of the user
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void connect(String hostname, int port, String database, boolean useSSL, String user, String pass) throws SQLException, ClassNotFoundException {
        if(conn == null && state == null){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL="+useSSL, user, pass);
            state = conn.createStatement();
        }
    }
}
