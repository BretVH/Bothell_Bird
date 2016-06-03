package bothell_bird;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Bret
 */
public class DisplayBird extends Bird {

    private final Feature conservationStatus;
    private final Feature family;
    private final Feature size;
    private final List<Feature> primaryColors;
    private final List<Feature> secondaryColors;
    private final List<Feature> habitats;
    private final List<Feature> locations;
    private final Feature billShape;
    private final Feature wingShape;
    //private final String description;

    public DisplayBird(Bird bird) throws SQLException, IOException {
        super(bird.getBirdId(), bird.getName(), bird.getConservationStatusId(), bird.getFamilyNameId(), bird.getSizeId(), bird.getBillShapeId(), bird.getWingShapeId());
        conservationStatus = BirdsListRetriever.getDisplayBirdFeature(getConservationStatusId(), "conservationStatus");
        family = BirdsListRetriever.getDisplayBirdFeature(getFamilyNameId(), "familyName");
        size = BirdsListRetriever.getDisplayBirdFeature(getSizeId(), "size");
        billShape = BirdsListRetriever.getDisplayBirdFeature(getBillShapeId(), "bill");
        wingShape = BirdsListRetriever.getDisplayBirdFeature(getWingShapeId(), "wing");
        this.primaryColors = setPrimaryColors();
        this.secondaryColors = setSecondaryColors();
        this.habitats = setHabitats();
        this.locations = setLocations();
    }

    public Feature getFamily() {
        return family;
    }

    public Feature getConservationStatus() {
        return conservationStatus;
    }

    public List<Feature> getPrimaryColors() {
        return primaryColors;
    }

    public List<Feature> getSecondaryColors() {
        return secondaryColors;
    }

    public List<Feature> getHabitats() {
        return habitats;
    }

    public List<Feature> getLocations() {
        return locations;
    }

    public Feature getWingShape() {
        return wingShape;
    }

    public Feature getBillShape() {
        return billShape;
    }

    @Override
    public String toString() {
        StringBuilder birdDescription = new StringBuilder();
        birdDescription.append("Species: ").append(getName());
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
        birdDescription.append("About ").append(getName()).append(":\n");
        //birdDescription.append(description);
        return birdDescription.toString();
    }

    private List<Feature> setPrimaryColors() throws SQLException {
        return ColorsRetriever.getPrimaryColors(getBirdId());
    }

    private List<Feature> setSecondaryColors() throws SQLException {
        return ColorsRetriever.getSecondaryColors(getBirdId());
    }

    private List<Feature> setHabitats() throws SQLException {
        return HabitatsRetriever.getHabitats(getBirdId());
    }

    private List<Feature> setLocations() throws SQLException {
        return LocationRetriever.getLocations(getBirdId());
    }
}
