package bothell_bird;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Bret Van Hof
 */
public class SimpleDataSource {
    private static Path currentWorkingDir = Paths.get("").toAbsolutePath();
    private static final String path = currentWorkingDir.toString().replace('\\', '/');
    //Temp home untill database is hosted on aws or Azure...
    //will also Export db to a file for offline consumption...
    private static final String url
            = "jdbc:ucanaccess://" + path + "/BothellBirdDatabase.accdb";
           // "jdbc:sqlserver://;serverName=2601:601:8900:1f3b:e10a:ecb4:9746:d1bd\\DOMECILE0";


    /**
     * Gets a connection to the database.
     *
     * @return the database connection
     * @throws java.sql.SQLException
     */
    public static Connection getconnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
