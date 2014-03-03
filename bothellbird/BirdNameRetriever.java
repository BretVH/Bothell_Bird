package bothellbird;

import java.sql.*;
import java.util.ArrayList;

public class BirdNameRetriever 
{
	private ArrayList<BirdName> bird;
    /**
     * reads data from an s.q.l server and creates an array list of 
     * Bird objects
     * @return list of birds
     * @throws SQLException 
     */    
	public ArrayList<BirdName> readData() throws SQLException
	{
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = null;
        stat = conn.createStatement();
        String query = "SELECT [nameID], [name], [uniqueBirdID] FROM" 
                + " BirdDatabase.dbo.name";  //get database table
        ResultSet rs = null;
        rs = stat.executeQuery(query);
        int i = 0; 
        while(rs.next())
        {
            i++;
        }
        bird = new ArrayList<BirdName>();
        
        rs = stat.executeQuery(query);
        rs.next();
        for(int j= 0; j < i; j++)//populate arraylist
        {    
        	BirdName aBirdName = new BirdName(rs.getString("Name"), 
            rs.getInt("nameID"), rs.getInt("uniqueBirdID"));
        	bird.add(aBirdName);
            rs.next();
        }
        conn.close();
     return bird;
	}
	
	public int getFeatureID(String name) throws SQLException
	{
	    int ID = 0;
	    if(name.equals("Primary Color"))
	    {
	    	ID = 2;
	    }
		else if(name.equals("Conservation Status"))
		{
			ID = 5;
		}
		else if(name.equals("Feeding Frequency"))
		{
			ID= 3;
		}
		else if(name.equals("Location"))
		{
			ID = 7;
		}
		else if(name.equals("Family"))
		{
			ID = 0;
		}
		else if(name.equals("Secondary Color"))
		{
			ID = 1;
		}
		else if(name.equals("Habitat"))
		{
			ID = 4;
		}
		else if(name.equals("Size"))
		{
			ID = 6;
		}
        return ID;
	}
	
	public ArrayList<BirdName> updateData(int feature, int iDS) throws SQLException
	{
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = null;
        stat = conn.createStatement();
        String featured = "";
        boolean fakeOut = false;
        boolean fakeOut2 = false;
        String featuredA = "UniqueFamilyID";
        String featuredB = "BirdSecondaryColor";
        switch(feature)
        {
        case 0:
        	fakeOut = true;
        	featured = "BirdFamilies";
        	break;
        case 1:
        	fakeOut2 = true;
        	featured = "BirdSecondaryColors";
        	break;
        case 2:
        	featured = "BirdPrimaryColor";
        	break;
        case 3:
        	featured = "BirdFeederFrequency";
        	break;
        case 4:
        	featured = "BirdHabitat";
        	break;
        case 5: 
        	featured = "BirdConservationStatus";
        	break;
        case 6:
        	featured = "BirdSize";
        	break;
        case 7:
        	featured = "BirdLocations";
        	break;
        }
        String query = "SELECT * FROM BirdDatabase.dbo." + featured + " Where " + featured + " = " + iDS; //get database table
    	if(fakeOut)
    		query = "SELECT * FROM BirdDatabase.dbo." + featured + " WHERE " + featuredA +  " = " + iDS;  //get database table
    	if(fakeOut2)
    		query = "SELECT * FROM BirdDatabase.dbo." + featured + " WHERE " + featuredB +  " = " + iDS;  //get database table
    	if(featured.equalsIgnoreCase("BirdLocations"))
    		query = "SELECT * FROM BirdDatabase.dbo." + featured + " WHERE BirdLocation = " + iDS;  //get database table
        ResultSet rs = null;
        rs = stat.executeQuery(query);
        int i = 0; 
        while(rs.next())
        {
            i++;
        }
        rs = stat.executeQuery(query);
        rs.next();
        ArrayList<Integer> birdIDS = new ArrayList<Integer>();
        for(int z = 0; z < i; z++)
        {
        	birdIDS.add(rs.getInt("uniqueBirdId"));
        	rs.next();
        }
        bird = new ArrayList<BirdName>();
        if(birdIDS.size() > 0)
        {
        String querye = "SELECT * FROM" 
                + " BirdDatabase.dbo.name where uniqueBirdID = " + birdIDS.get(0);  //get database table
        for(int y = 1; y < birdIDS.size(); y++)
        {
        	querye += " or uniqueBirdId = " + birdIDS.get(y);
        }
        rs = stat.executeQuery(querye);
        i = 0;
        while(rs.next())
        {
        	i++;
        }
        rs = stat.executeQuery(querye);
        rs.next();
        for(int j= 0; j < i; j++)//populate arraylist
        {    
        	BirdName aBirdName = new BirdName(rs.getString("name"), 
            rs.getInt("nameID"), rs.getInt("uniqueBirdID"));
        	bird.add(aBirdName);
            rs.next();
        }
        }
        conn.close();
     return bird;
	}
	
	public String getScientificName(int id) throws SQLException
	{
		Connection conn = SimpleDataSource.getconnection();
        Statement stat = null;
        stat = conn.createStatement();
        String query = "SELECT [scientificName] FROM" 
                + " BirdDatabase.dbo.BirdID where id = '" + id + "'";  //get database table
        ResultSet rs = null;
        rs = stat.executeQuery(query);
        rs.next();
        String sciName = rs.getString("scientificName");
        conn.close();
        return sciName;
	}
	
	public ArrayList<String> getCommonNames(int id) throws SQLException
	{
		ArrayList<String> common = new ArrayList<String>();
		Connection conn = SimpleDataSource.getconnection();
        Statement stat = null;
        stat = conn.createStatement();
        String query = "SELECT [name] FROM" 
                + " BirdDatabase.dbo.BirdID where id = '" + id + "'";  //get database table
        ResultSet rs = null;
        rs = stat.executeQuery(query);
        rs.next();
        for(int j= 0; j < common.size(); j++)//populate arraylist
        {    
        	common.add(rs.getString("name"));
            rs.next();
        }
        conn.close();
        return common;
	}
	
	
}
