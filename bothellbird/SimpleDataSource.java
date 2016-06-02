package bothell_bird;


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
public class SimpleDataSource {

    //Temp home untill database is hosted on aws or Azure...
    //will also Export db to a file for offline consumption...
    private static final String url
            = "jdbc:sqlserver://;serverName=2601:601:8900:1f3b:e10a:ecb4:9746:d1bd\\DOMECILE0";
    final private static String username = "BBUSer";
    final private static String password = "Bothellbird";

    /**
     * Gets a connection to the database.
     *
     * @return the database connection
     * @throws java.sql.SQLException
     */
    public static Connection getconnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
