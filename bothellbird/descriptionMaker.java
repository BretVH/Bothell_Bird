package bothellbird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class descriptionMaker
{
	private Map<Integer, String> features = new TreeMap<Integer, String>();
	String[] queries = new String[8];
	String[] queries2 = new String[8];
	private ArrayList<String> descriptions = new ArrayList<String>();

	public descriptionMaker() 
	{

	}

	public String getDescription(int ID) throws SQLException
	{

		String description = "";
		for(int i = 0; i < queries.length; i++)
		{
			createQueries();
			process(i);
			getBirdFeatures(ID, i);
			features.clear();
		}

		for(String bird: descriptions)
		{
			description += bird + "\r\n";
		}
		return description;
	}

	public void processAgain(int j) throws SQLException
	{
		int i = 0;
		Connection conn = SimpleDataSource.getconnection();
		Statement stat = null;
		stat = conn.createStatement();
		ResultSet rs = null;
		rs = stat.executeQuery(queries2[j]);
		String temp = "";
		switch(j)
		{
		case 0:
			temp += "Bird's Family: ";
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{    
				if(features.get(rs.getInt("UniqueFamilyID")) != null && k != i - 1)
					temp += features.get(rs.getInt("UniqueFamilyID")) + ", ";
				else
					temp += features.get(rs.getInt("UniqueFamilyID"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		case 1:
			temp += "Bird's Secondary Color: ";
			while(rs.next())
			{
				i++;
			}
			rs.close();
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{
				if(features.get(rs.getInt("BirdSecondaryColor")) != null && k != i - 1)
					temp += features.get(rs.getInt("BirdSecondaryColor")) + ", ";
				else
					temp += features.get(rs.getInt("BirdSecondaryColor"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		case 2:
			temp += "Bird's Primary Color: ";
			while(rs.next())
			{
				i++;
			}
			rs.close();
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{    
				if(features.get(rs.getInt("birdPrimaryColor")) != null && k != i - 1)
					temp += features.get(rs.getInt("birdPrimaryColor")) + ", ";
				else
					temp += features.get(rs.getInt("birdPrimaryColor"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		case 3:
			temp += "Bird's Backyard Feeder Frequency: ";
			while(rs.next())
			{
				i++;
			}
			rs.close();
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{    
				if(features.get(rs.getInt("BirdFeederFrequency")) != null && k != i - 1)
					temp += features.get(rs.getInt("BirdFeederFrequency")) + ", ";
				else
					temp += features.get(rs.getInt("BirdFeederFrequency"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		case 4:
			temp += "Bird's Habitat: ";
			while(rs.next())
			{
				i++;
			}
			rs.close();
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{    
				if(features.get(rs.getInt("BirdHabitat")) != null && k != i - 1)
					temp += features.get(rs.getInt("BirdHabitat")) + ", ";
				else
					temp += features.get(rs.getInt("BirdHabitat"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		case 5:
			temp += "Bird's Conservation Status: ";
			while(rs.next())
			{
				i++;
			}
			rs.close();
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{    
				if(features.get(rs.getInt("BirdConservationStatus")) != null && k != i - 1)
					temp += features.get(rs.getInt("BirdConservationStatus")) + ", ";
				else
					temp += features.get(rs.getInt("BirdConservationStatus"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		case 6:
			temp += "Bird's Size: ";
			while(rs.next())
			{
				i++;
			}
			rs.close();
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{    
				if(features.get(rs.getInt("BirdSize")) != null && k != i - 1)
					temp += features.get(rs.getInt("BirdSize")) + ", ";
				else
					temp += features.get(rs.getInt("BirdSize"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		case 7:
			temp += "Bird's Location: ";
			while(rs.next())
			{
				i++;
			}
			rs.close();
			rs = stat.executeQuery(queries2[j]);
			rs.next();
			for(int k= 0; k < i; k++)//populate arraylist
			{    
				if(features.get(rs.getInt("BirdLocation")) != null && k != i - 1)
					temp += features.get(rs.getInt("BirdLocation")) + ", ";
				else
					temp += features.get(rs.getInt("BirdLocation"));
				rs.next();
			}
			descriptions.add(temp);
			break;
		default:
			break;
		}
		conn.close();
	}

	public void process(int j) throws SQLException
	{
		int i = 0;
		Connection conn = SimpleDataSource.getconnection();
		Statement stat = null;
		stat = conn.createStatement();
		ResultSet rs = null;
		switch(j)
		{
		case 0:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("familynameID"), 
						rs.getString("familyname"));
				rs.next();
			}
			break;
		case 1:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("SecondaryColorID"), 
						rs.getString("secondaryColor"));
				rs.next();
			}
			break;
		case 2:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("PColorIDs"), 
						rs.getString("PrimaryColors"));
				rs.next();
			}
			break;
		case 3:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("FeederFrequencyId"), 
						rs.getString("FeederFrequency"));
				rs.next();
			}
			break;
		case 4:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("HabitatId"), 
						rs.getString("habitatName"));
				rs.next();
			}
			break;
		case 5:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("ConservationStatusID"), 
						rs.getString("ConservationStatus"));
				rs.next();
			}
			break;
		case 6:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("SizeId"), 
						rs.getString("Size"));
				rs.next();
			}
			break;
		case 7:
			rs = stat.executeQuery(queries[j]);
			while(rs.next())
			{
				i++;
			}
			rs = stat.executeQuery(queries[j]);
			rs.next();
			for(int l = 0; l < i; l++)//populate arraylist
			{    
				features.put(rs.getInt("LocationID"), 
						rs.getString("locationName"));
				rs.next();
			}
			break;
		default:
			break;
		}
		conn.close();
	}

	public void createQueries()
	{
		queries[0] = "SELECT [familynameID], [familyname] FROM" 
				+ " BirdDatabase.dbo.Family";
		queries[1] = "SELECT [SecondaryColorID], [secondaryColor] FROM" 
				+ " BirdDatabase.dbo.SecondaryColor";
		queries[2] = "SELECT [PColorIDs], [PrimaryColors] FROM" 
				+ " BirdDatabase.dbo.PrimaryColors";
		queries[3] = "SELECT [FeederFrequencyId], [FeederFrequency] FROM" 
				+ " BirdDatabase.dbo.FeederFrequency";
		queries[4] = "SELECT [HabitatId], [habitatName] FROM" 
				+ " BirdDatabase.dbo.Habitats";
		queries[5] = "SELECT [ConservationStatusID], [ConservationStatus] FROM" 
				+ " BirdDatabase.dbo.ConservationStatus";
		queries[6] = "SELECT [SizeId], [Size] FROM" 
				+ " BirdDatabase.dbo.size";
		queries[7] = "SELECT [LocationID], [locationName] FROM" 
				+ " BirdDatabase.dbo.Locations";
	}


	public void evenMoreQueries(int id)
	{
		queries2[0] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdFamilies where uniqueBirdId = " + id;
		queries2[1] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdSecondaryColors where uniqueBirdId = " + id;
		queries2[2] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdPrimaryColor where uniqueBirdId = " + id;
		queries2[3] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdFeederFrequency where uniqueBirdId = " + id;
		queries2[4] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdHabitat where uniqueBirdId = " + id;
		queries2[5] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdConservationStatus where uniqueBirdId = " + id;
		queries2[6] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdSize where uniqueBirdId = " + id;
		queries2[7] = "SELECT * FROM" 
				+ " BirdDatabase.dbo.BirdLocations where uniqueBirdId = " + id;

	}

	public void getBirdFeatures(int ID, int i) throws SQLException
	{

		evenMoreQueries(ID);		
		processAgain(i);	
	}

	public Map<String, ArrayList<String>> getFeatures() throws SQLException
	{
		createQueries();
		Map<String, ArrayList<String>> stuff = new HashMap<String, ArrayList<String>>();
		Connection conn = SimpleDataSource.getconnection();
		Statement stat = null;
		stat = conn.createStatement();
		ResultSet rs = null;
		for(int i = 0; i < 8; i++)
		{
			rs = stat.executeQuery(queries[i]);
			rs.next();
			stuff = aProcess(i, rs, stat, stuff);
		}
		conn.close();
		return stuff;
	}

	public Map<String, ArrayList<String>> aProcess
	(int i, ResultSet rs, Statement stat, Map<String, ArrayList<String>> stuff) throws SQLException
	{
		int j = 0;
		switch(i)
		{
		case 0:
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> feature = new ArrayList<String>();
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			rs.next();
			for(int l = 0; l < j; l++)//populate arraylist
			{    
				feature.add(rs.getString("familyname"));
				feature.add(rs.getInt("familynameID") + "");
				rs.next();
			}
			stuff.put("Family", feature);
			break;
		case 1:
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> featureA = new ArrayList<String>();
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			rs.next();
			for(int l = 0; l < j; l++)//populate arraylist
			{    
				featureA.add(rs.getString("secondaryColor"));
				featureA.add(rs.getInt("SecondaryColorID") + "");
				rs.next();
			}
			stuff.put("Secondary Color", featureA);
			break;
		case 2:
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> featureB = new ArrayList<String>();
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			rs.next();
			for(int l= 0; l < j; l++)//populate arraylist
			{    
				featureB.add(rs.getString("PrimaryColors"));
				featureB.add(rs.getInt("PColorIDs") + "");
				rs.next();
			}
			stuff.put("Primary Color", featureB);
			break;
		case 3:
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> featureC = new ArrayList<String>();
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			rs.next();
			for(int l= 0; l < j; l++)//populate arraylist
			{    
				featureC.add(rs.getString("FeederFrequency"));
				featureC.add(rs.getInt("FeederFrequencyID") + "");
				rs.next();
			}
			stuff.put("Feeding Frequency", featureC);
			break;
		case 4:
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> featureD = new ArrayList<String>();
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			rs.next();
			for(int l= 0; l < j; l++)//populate arraylist
			{    
				featureD.add(rs.getString("habitatName"));
				featureD.add(rs.getInt("HabitatId") + "");
				rs.next();
			}
			stuff.put("Habitat", featureD);
			break;
		case 5:
			rs = stat.executeQuery(queries[i]);
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> featureE = new ArrayList<String>();
			rs.next();
			for(int l= 0; l < j; l++)//populate arraylist
			{    
				featureE.add(rs.getString("ConservationStatus"));
				featureE.add(rs.getInt("ConservationStatusID") + "");
				rs.next();
			}
			stuff.put("Conservation Status", featureE);
			break;
		case 6:
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> featureF = new ArrayList<String>();
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			rs.next();
			for(int l= 0; l < j; l++)//populate arraylist
			{    
				featureF.add(rs.getString("size"));
				featureF.add(rs.getInt("SizeID") + "");
				rs.next();
			}
			stuff.put("Size", featureF);
			break;
		case 7:
			rs = stat.executeQuery(queries[i]);
			ArrayList<String> featureG = new ArrayList<String>();
			while(rs.next())
			{
				j++;
			}
			rs = stat.executeQuery(queries[i]);
			rs.next();
			for(int l= 0; l < j; l++)//populate arraylist
			{    
				featureG.add(rs.getString("locationName"));
				featureG.add(rs.getInt("LocationID") + "");
				rs.next();
			}
			stuff.put("Location", featureG);
			break;
		default:
			break;
		}
		return stuff;
	}

}
