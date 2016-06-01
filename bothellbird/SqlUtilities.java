/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bothell_bird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Bret
 */
public class SqlUtilities {

    public static int getFeatureCount(String tn) throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM BirdDatabase.dbo." + tn;
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(countQuery);
        int numberOfFeatures = 0;
        while (rs.next()) {
            numberOfFeatures = Integer.parseInt(rs.getString("count(*)"));
        }
        return numberOfFeatures;
    }
}
