package bothell_bird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bret
 */
public class FiltersToFeaturesRetriever {

    private static final String[] queriesForSelectableFeatures = {
        "SELECT * FROM Locations",
        "SELECT * FROM Habitats",
        "SELECT * FROM Size",
        "SELECT * FROM Wing",
        "SELECT * FROM Bill",
        "SELECT * FROM PrimaryColors",
        "SELECT * FROM SecondaryColor",
        "SELECT * FROM Family",
        "SELECT * FROM ConservationStatus",};

    private static final String[] tableNames = {
        "Locations",
        "Habitats",
        "Size",
        "Wing",
        "Bill",
        "PrimaryColors",
        "SecondaryColor",
        "Family",
        "ConservationStatus"
    };

    public static Map<String, List<Feature>> getFilterToFeaturesMap() throws SQLException {
        int counter = 0;
        Map<String, List<Feature>> filterToSelectableFeature = new LinkedHashMap<>();
        for (String query : queriesForSelectableFeatures) {
            String filter = tableNames[counter];
            List<Feature> feature = new ArrayList<>();
            switch (counter) {
                case 0:
                    feature = getLocations(filter, query);
                    break;
                case 1:
                    feature = getHabitats(filter, query);
                    break;
                case 2:
                    feature = getSizes(filter, query);
                    break;
                case 3:
                    feature = getWings(filter, query);
                    break;
                case 4:
                    feature = getBills(filter, query);
                    break;
                case 5:
                    feature = getPrimaryColors(filter, query);
                    break;
                case 6:
                    feature = getSecondaryColors(filter, query);
                    break;
                case 7:
                    feature = getFamilies(filter, query);
                    break;
                case 8:
                    feature = getConservationStatuses(filter, query);
                    break;
            }
            filterToSelectableFeature.put(filter, feature);
            counter++;
        }
        return filterToSelectableFeature;
    }

    private static List<Feature> getFamilies(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "familyName", "familyNameId");
    }

    private static List<Feature> getSecondaryColors(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "secondaryColor", "secondaryColorId");
    }

    private static List<Feature> getPrimaryColors(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "primaryColor", "primaryColorId");
    }

    private static List<Feature> getHabitats(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "habitatNames", "habitatId");
    }

    private static List<Feature> getConservationStatuses(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "conservationStatus", "conservationStatusId");
    }

    private static List<Feature> getSizes(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "size", "sizeId");
    }

    private static List<Feature> getLocations(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "location", "locationId");
    }

    private static List<Feature> getWings(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "wing", "wingId");
    }

    private static List<Feature> getBills(String tn, String q) throws SQLException {
        return getFeatureList(tn, q, "bill", "billId");
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
