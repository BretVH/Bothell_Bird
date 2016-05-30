package bothell_bird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DescriptionRetriever {

    private static final Map<Integer, String> features = new HashMap<>();
    private static final String[] queries = new String[8];
    private static final String[] queries2 = new String[8];
    private static final ArrayList<String> descriptions = new ArrayList<>();


    public static String getDescription(int ID) throws SQLException {

        String description = "";
        for (int i = 0; i < queries.length; i++) {
            createQueries();
            process(i);
            getBirdFeatures(ID, i);
            features.clear();
        }

        for (String bird : descriptions) {
            description += bird + "\r\n";
        }
        return description;
    }

    private static void processAgain(int j) throws SQLException {
        int i = 0;
        try (Connection conn = SimpleDataSource.getconnection()) {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(queries2[j]);
            String temp = "";
            switch (j) {
                case 0:
                    temp += "Bird's Family: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("UniqueFamilyID")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("UniqueFamilyID")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("UniqueFamilyID"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                case 1:
                    temp += "Bird's Secondary Color: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs.close();
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("BirdSecondaryColor")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("BirdSecondaryColor")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("BirdSecondaryColor"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                case 2:
                    temp += "Bird's Primary Color: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs.close();
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("birdPrimaryColor")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("birdPrimaryColor")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("birdPrimaryColor"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                case 3:
                    temp += "Bird's Backyard Feeder Frequency: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs.close();
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("BirdFeederFrequency")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("BirdFeederFrequency")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("BirdFeederFrequency"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                case 4:
                    temp += "Bird's Habitat: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs.close();
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("BirdHabitat")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("BirdHabitat")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("BirdHabitat"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                case 5:
                    temp += "Bird's Conservation Status: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs.close();
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("BirdConservationStatus")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("BirdConservationStatus")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("BirdConservationStatus"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                case 6:
                    temp += "Bird's Size: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs.close();
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("BirdSize")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("BirdSize")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("BirdSize"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                case 7:
                    temp += "Bird's Location: ";
                    while (rs.next()) {
                        i++;
                    }
                    rs.close();
                    rs = stat.executeQuery(queries2[j]);
                    rs.next();
                    for (int k = 0; k < i; k++)//populate arraylist
                    {
                        if (features.get(rs.getInt("BirdLocation")) != null && k != i - 1) {
                            temp += features.get(rs.getInt("BirdLocation")) + ", ";
                        } else {
                            temp += features.get(rs.getInt("BirdLocation"));
                        }
                        rs.next();
                    }
                    descriptions.add(temp);
                    break;
                default:
                    break;
            }
        }
    }

    private static void process(int j) throws SQLException {
        int i = 0;
        try (Connection conn = SimpleDataSource.getconnection()) {
            Statement stat = conn.createStatement();
            ResultSet rs;
            switch (j) {
                case 0:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("familynameID"),
                                rs.getString("familyname"));
                        rs.next();
                    }
                    break;
                case 1:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("SecondaryColorID"),
                                rs.getString("secondaryColor"));
                        rs.next();
                    }
                    break;
                case 2:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("PColorIDs"),
                                rs.getString("PrimaryColors"));
                        rs.next();
                    }
                    break;
                case 3:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("FeederFrequencyId"),
                                rs.getString("FeederFrequency"));
                        rs.next();
                    }
                    break;
                case 4:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("HabitatId"),
                                rs.getString("habitatName"));
                        rs.next();
                    }
                    break;
                case 5:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("ConservationStatusID"),
                                rs.getString("ConservationStatus"));
                        rs.next();
                    }
                    break;
                case 6:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("SizeId"),
                                rs.getString("Size"));
                        rs.next();
                    }
                    break;
                case 7:
                    rs = stat.executeQuery(queries[j]);
                    while (rs.next()) {
                        i++;
                    }
                    rs = stat.executeQuery(queries[j]);
                    rs.next();
                    for (int l = 0; l < i; l++)//populate arraylist
                    {
                        features.put(rs.getInt("LocationID"),
                                rs.getString("locationName"));
                        rs.next();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static void createQueries() {
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

    private static void evenMoreQueries(int id) {
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

    public static void getBirdFeatures(int ID, int i) throws SQLException {

        evenMoreQueries(ID);
        processAgain(i);
    }

    public static Map<String, ArrayList<String>> getFeatures() throws SQLException {
        createQueries();
        Map<String, ArrayList<String>> featureNameToBirdFeaturesMap = new HashMap<>();
        try (Connection conn = SimpleDataSource.getconnection()) {
            Statement stat =  conn.createStatement();
            ResultSet rs;
            for (int i = 0; i < 8; i++) {
                rs = stat.executeQuery(queries[i]);
                rs.next();
                featureNameToBirdFeaturesMap = aProcess(i, rs, stat, featureNameToBirdFeaturesMap);
            }
        }
        return featureNameToBirdFeaturesMap;
    }

    private static Map<String, ArrayList<String>> aProcess(int i, ResultSet rs, Statement stat, Map<String, ArrayList<String>> featureNameToBirdFeaturesMap) throws SQLException {
        int j = 0;
        switch (i) {
            case 0:
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> birdFamilyNames = new ArrayList<>();
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    birdFamilyNames.add(rs.getString("familyname"));
                    birdFamilyNames.add(rs.getInt("familynameID") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Family", birdFamilyNames);
                break;
            case 1:
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> secondaryBirdColors = new ArrayList<>();
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    secondaryBirdColors.add(rs.getString("secondaryColor"));
                    secondaryBirdColors.add(rs.getInt("SecondaryColorID") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Secondary Color", secondaryBirdColors);
                break;
            case 2:
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> primaryBirdColors = new ArrayList<>();
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    primaryBirdColors.add(rs.getString("PrimaryColors"));
                    primaryBirdColors.add(rs.getInt("PColorIDs") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Primary Color", primaryBirdColors);
                break;
            case 3:
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> feedingFrequencies = new ArrayList<>();
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    feedingFrequencies.add(rs.getString("FeederFrequency"));
                    feedingFrequencies.add(rs.getInt("FeederFrequencyID") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Feeding Frequency", feedingFrequencies);
                break;
            case 4:
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> habitats = new ArrayList<>();
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    habitats.add(rs.getString("habitatName"));
                    habitats.add(rs.getInt("HabitatId") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Habitat", habitats);
                break;
            case 5:
                rs = stat.executeQuery(queries[i]);
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> conservationStatuses = new ArrayList<>();
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    conservationStatuses.add(rs.getString("ConservationStatus"));
                    conservationStatuses.add(rs.getInt("ConservationStatusID") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Conservation Status", conservationStatuses);
                break;
            case 6:
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> birdSize = new ArrayList<>();
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    birdSize.add(rs.getString("size"));
                    birdSize.add(rs.getInt("SizeID") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Size", birdSize);
                break;
            case 7:
                rs = stat.executeQuery(queries[i]);
                ArrayList<String> locations = new ArrayList<>();
                while (rs.next()) {
                    j++;
                }
                rs = stat.executeQuery(queries[i]);
                rs.next();
                for (int l = 0; l < j; l++)//populate arraylist
                {
                    locations.add(rs.getString("locationName"));
                    locations.add(rs.getInt("LocationID") + "");
                    rs.next();
                }
                featureNameToBirdFeaturesMap.put("Location", locations);
                break;
            default:
                break;
        }
        return featureNameToBirdFeaturesMap;
    }

}
