package bothell_bird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bret
 */
public class FiltersToFeaturesRetriever {

    private static final String[] queriesForSelectableFeatures = {
        "SELECT * FROM BirdDatabase.dbo.Family",
        "SELECT * FROM BirdDatabase.dbo.SecondaryColor",
        "SELECT * FROM BirdDatabase.dbo.PrimaryColors",
        "SELECT * FROM BirdDatabase.dbo.Habitats",
        "SELECT * FROM BirdDatabase.dbo.ConservationStatus",
        "SELECT * FROM BirdDatabase.dbo.Size",
        "SELECT * FROM BirdDatabase.dbo.Locations"
    };

    private static final String[] tableNames = {
        "Family",
        "SecondaryColor",
        "PrimaryColors",
        "Habitats",
        "ConservationStatus",
        "Size",
        "Locations"
    };

    public static Map<String, List<Feature>> getFilterToFeaturesMap() throws SQLException {
        int counter = 0;
        Map<String, List<Feature>> filterToSelectableFeature = new HashMap<>();
        for (String query : queriesForSelectableFeatures) {

            String filter = tableNames[counter];
            List<Feature> feature = new ArrayList<>();
            switch (counter) {
                case 0:
                    feature = getFamilies(tableNames[counter], counter, query);
                    break;
                case 1:
                    feature = getSecondaryColors(tableNames[counter], counter, query);
                    break;
                case 2:
                    feature = getPrimaryColors(tableNames[counter], counter, query);
                    break;
                case 3:
                    feature = getHabitats(tableNames[counter], counter, query);
                    break;
                case 4:
                    feature = getConservationStatuses(tableNames[counter], counter, query);
                    break;
                case 5:
                    feature = getSizes(tableNames[counter], counter, query);
                    break;
                case 6:
                    feature = getLocations(tableNames[counter], counter, query);
                    break;
            }
            filterToSelectableFeature.put(tableNames[counter], feature);
            counter++;
        }
        return filterToSelectableFeature;
    }

    private static List<Feature> getFamilies(String tn, int i, String q) throws SQLException {
        return getFeatureList(tn, q, "familyName", "familyNameId");
    }

    private static List<Feature> getSecondaryColors(String tn, int i, String q) throws SQLException {
        return getFeatureList(tn, q, "secondaryColor", "secondaryColorId");
    }

    private static List<Feature> getPrimaryColors(String tn, int i, String q) throws SQLException {
        return getFeatureList(tn, q, "primaryColor", "primaryColorId");
    }

    private static List<Feature> getHabitats(String tn, int i, String q) throws SQLException {
        return getFeatureList(tn, q, "habitatNames", "habitatId");
    }

    private static List<Feature> getConservationStatuses(String tn, int i, String q) throws SQLException {
        return getFeatureList(tn, q, "conservationStatus", "conservationStatusId");
    }

    private static List<Feature> getSizes(String tn, int i, String q) throws SQLException {
        return getFeatureList(tn, q, "size", "sizeId");
    }

    private static List<Feature> getLocations(String tn, int i, String q) throws SQLException {
        return getFeatureList(tn, q, "location", "locationId");
    }

    private static List<Feature> getFeatureList(String tn, String q, String n, String id) throws SQLException {
        int count = SqlUtilities.getFeatureCount(tn);
        List<Feature> featureList = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(q);
        rs.next();
        for (int i = 0; i < count; i++) {
            int fId = rs.getInt(id);
            String name = rs.getString(n);
            Feature feature = new Feature(fId, name);
            featureList.add(feature);
            rs.next();
        }
        return featureList;
    }

}
