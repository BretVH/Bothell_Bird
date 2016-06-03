package bothell_bird;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.ImageIcon;

//TODO: implement Comparable, override equals, hashCode, toString
public final class Bird implements Comparable<Bird>, Comparator<Bird> {

    private final int birdId;
    private final String scientificName;
    private final Feature conservationStatus;
    private final Feature family;
    private final Feature size;
    private final List<BirdName> names;
    private final List<Feature> primaryColors;
    private final List<Feature> secondaryColors;
    private final List<Feature> habitats;
    private final List<Feature> locations;
    private final ImageIcon icon;
    private final Feature billShape;
    private final Feature wingShape;
    private final String description;

    public Bird(int birdId, String name, Feature cs, Feature fn, Feature s, Feature bs, Feature ws, String d) throws SQLException, IOException {
        this.birdId = birdId;
        this.scientificName = name;
        this.conservationStatus = cs;
        this.family = fn;
        this.size = s;
        this.billShape = bs;
        this.wingShape = ws;
        this.names = setNames();
        this.primaryColors = setPrimaryColors();
        this.secondaryColors = setSecondaryColors();
        this.icon = ImageRetriever.getImageIcon(birdId, 'n', false);
        this.habitats = setHabitats();
        this.locations = setLocations();
        this.description = d;
    }

    public int getBirdId() {
        return birdId;
    }

    public String getName() {
        return scientificName;
    }

    public Feature getSize() {
        return size;
    }

    public int getSizeId() {
        return size.getFeatureId();
    }

    public Feature getFamily() {
        return family;
    }

    public int getFamilyNameId() {
        return family.getFeatureId();
    }

    public Feature getConservationStatus() {
        return conservationStatus;
    }

    public int getConservationStatusId() {
        return conservationStatus.getFeatureId();
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public BirdName getNeutralName() {
        for (BirdName name : names) {
            if (name.getGender() == 'n') {
                return name;
            }
        }
        return new BirdName('n', scientificName, 0, birdId);
    }

    public List<BirdName> getNames() {
        return names;
    }

    public List<Integer> getPrimaryColorIds() {
        List<Integer> primaryColorIds = new ArrayList<>();
        for (Feature color : primaryColors) {
            primaryColorIds.add(color.getFeatureId());
        }
        return primaryColorIds;
    }

    public List<Feature> getPrimaryColors() {
        return primaryColors;
    }

    public List<Integer> getSecondaryColorIds() {
        List<Integer> secondaryColorIds = new ArrayList<>();
        for (Feature color : secondaryColors) {
            secondaryColorIds.add(color.getFeatureId());
        }
        return secondaryColorIds;
    }

    public List<Feature> getSecondaryColors() {
        return secondaryColors;
    }

    public List<Integer> getHabitatIds() {
        List<Integer> habitatIds = new ArrayList<>();
        for (Feature habitat : habitats) {
            habitatIds.add(habitat.getFeatureId());
        }
        return habitatIds;
    }

    public List<Feature> getHabitats() {
        return habitats;
    }

    public List<Integer> getLocationIds() {
        List<Integer> locationIds = new ArrayList<>();
        for (Feature location : locations) {
            locationIds.add(location.getFeatureId());
        }
        return locationIds;
    }

    public List<Feature> getLocations() {
        return locations;
    }

    public Feature getWingShape() {
        return wingShape;
    }

    public int getWingShapeId() {
        return wingShape.getFeatureId();
    }

    public Feature getBillShape() {
        return billShape;
    }

    public int getBillShapeId() {
        return billShape.getFeatureId();
    }

    @Override
    public int compareTo(Bird other) {
        return birdId - other.getBirdId();
    }

        @Override
    public int compare(Bird b1, Bird b2) {
        return b1.compareTo(b2);
    }
    
    @Override
    public String toString() {
        StringBuilder birdDescription = new StringBuilder();
        birdDescription.append("Species: ").append(scientificName);
        birdDescription.append("\nFamily: ").append(family.getFeatureName());
        birdDescription.append("\nConservation Status: ").append(conservationStatus.getFeatureName());
        birdDescription.append("\nHabitat: ");
        for (Feature habitat : habitats) {
            birdDescription.append(habitat.getFeatureName()).append(" ");
        }
        birdDescription.append("\nLocations: ");
        for (Feature location : locations) {
            birdDescription.append(location.getFeatureName()).append(" ");
        }
        birdDescription.append("\nSize: ").append(size.getFeatureName());
        birdDescription.append("\nWing Shape: ").append(wingShape.getFeatureName());
        birdDescription.append("\nBill Shape: ").append(billShape.getFeatureName());
        birdDescription.append("\nPrimary Colors: ");
        for (Feature color : primaryColors) {
            birdDescription.append(color.getFeatureName()).append(" ");
        }
        birdDescription.append("\nSecondary Colors: ");
        for (Feature color : secondaryColors) {
            birdDescription.append(color.getFeatureName());
        }
        birdDescription.append("About ").append(scientificName).append(":\n");
        birdDescription.append(description);
        return birdDescription.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Bird))
            return false;
        if (other == this)
            return true;
        return ((Bird)other).getBirdId() == birdId;
    }
    
    @Override
    public int hashCode() {
        return birdId;
    }
    
    private List<BirdName> setNames() throws SQLException {
        return BirdNamesRetriever.getAliasList(birdId);
    }

    private List<Feature> setPrimaryColors() throws SQLException {
        return ColorsRetriever.getPrimaryColors(birdId);
    }

    private List<Feature> setSecondaryColors() throws SQLException {
        return ColorsRetriever.getSecondaryColors(birdId);
    }

    private List<Feature> setHabitats() throws SQLException {
        return HabitatsRetriever.getHabitats(birdId);
    }

    private List<Feature> setLocations() throws SQLException {
        return LocationRetriever.getLocations(birdId);
    }
}
