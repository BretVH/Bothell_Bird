package bothellbird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenderRetriever 
{
	private LinkedHashMap<String, String> gender = new LinkedHashMap<String, String>();
	
	public GenderRetriever() 
	{
	}
	
	public LinkedHashMap<String, String> getGender(int id) throws SQLException
	{
		Connection conn = SimpleDataSource.getconnection();
        Statement stat = null;
        stat = conn.createStatement();
        String query = "SELECT [gender], [uniqueBirdName] FROM" 
                + " BirdDatabase.dbo.Gender where uniqueBirdId = '" + id + "'";  //get database table
        ResultSet rs = null;
        rs = stat.executeQuery(query);
        int i = 0; 
        while(rs.next())
        {
            i++;
        }        
        rs = stat.executeQuery(query);
        rs.next();
        Map<Integer, String> genders = new LinkedHashMap<Integer,String>();
        int[] name = new int[i];
        for(int j= 0; j < i; j++)//populate arraylist
        {    
        	genders.put(rs.getInt("uniqueBirdName"),rs.getString("gender"));
        	name[j] = rs.getInt("uniqueBirdName");
        }
        for(int j= 0; j < i; j++)//populate arraylist
        {    
        	String query2 = "SELECT [name] FROM" 
                    + " BirdDatabase.dbo.name where nameId = " + 
        			name[j];
        	ResultSet a = stat.executeQuery(query2);
        	a.next();
        	gender.put(a.getString("name"), genders.get(name[j]));
        }
        conn.close();
     return gender;
	}
	
	public int getNameId(int id, String bName) throws SQLException
	{
		Connection conn = SimpleDataSource.getconnection();
        Statement stat = null;
        stat = conn.createStatement();
        String query = "Select [nameID] from BirdDatabase.dbo.name where name = '" + bName + "'";
        ResultSet rs = stat.executeQuery(query);
        rs.next();
        return rs.getInt("nameID");
	}
}

