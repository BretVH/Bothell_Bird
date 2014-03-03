package bothellbird;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Bret Van Hof
 */
public class SimpleDataSource 
{
    private static String url = 
            "jdbc:sqlserver://192.168.1.1 ";
    final private static String username = "BBUSer";
    final private static String password = "Bothellbird";
    
    /**
      Gets a connection to the database.
     * @return the database connection
     */            
    public static Connection getconnection() throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }
}
