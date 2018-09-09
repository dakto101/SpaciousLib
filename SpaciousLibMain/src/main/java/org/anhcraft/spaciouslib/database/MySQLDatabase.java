package org.anhcraft.spaciouslib.database;

import org.anhcraft.spaciouslib.utils.ExceptionThrower;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase extends Database{
    /**
     * Creates a new connection to a MySQL database
     * @param host the IP address or hostname of its MySQL server (the default is localhost or 127.0.0.1)
     * @param port the TCP/IP port which is listening by its MySQL server (the default is 3306)
     * @param database the name of the database
     * @param useSSL uses to enable SSL encryption
     * @param user the name of an user
     * @param pass the password of an user
     */
    public void connect(String host, int port, String database, boolean useSSL, String user, String pass) throws SQLException, ClassNotFoundException {
        ExceptionThrower.ifNull(host, new Exception("Hostname must not null"));
        ExceptionThrower.ifNull(database, new Exception("Database name must not null"));
        ExceptionThrower.ifNull(user, new Exception("Username must not null"));
        ExceptionThrower.ifNull(pass, new Exception("Password must not null"));
        if(conn == null && state == null){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL="+useSSL, user, pass);
            state = conn.createStatement();
        }
    }
}
