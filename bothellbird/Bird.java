package bothell_bird;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import javax.swing.ImageIcon;

public class Bird implements Comparable<Bird>, Comparator<Bird> {

    private final int birdId;
    private final String scientificName;
    private final int conservationStatusId;
    private final int familyId;
    private final int sizeId;
    private final List<BirdName> names;
    private final List<Integer> primaryColorIds;
    private final List<Integer> secondaryColorIds;
    private final List<Integer> habitatIds;
    private final List<Integer> locationIds;
    private final ImageIcon icon;
    private final int billShapeId;
    private final int wingShapeId;

    public Bird(int birdId, String name, int csId, int fId, int sId, int bsId, int wsId) throws SQLException, IOException {
        this.birdId = birdId;
        this.scientificName = name;
        this.conservationStatusId = csId;
        this.familyId = fId;
        this.sizeId = sId;
        this.billShapeId = bsId;
        this.wingShapeId = wsId;
        this.names = setNames();
        this.primaryColorIds = setPrimaryColorIds();
        this.secondaryColorIds = setSecondaryColorIds();
        this.icon = ImageRetriever.getImageIcon(birdId, 'n', false);
        this.habitatIds = setHabitatIds();
        this.locationIds = setLocationIds();
    }

    public int getBirdId() {
        return birdId;
    }

    public String getName() {
        return scientificName;
    }

    public int getSizeId() {
        return sizeId;
    }

    public int getFamilyNameId() {
        return familyId;
    }

    public int getConservationStatusId() {
        return conservationStatusId;
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
        return primaryColorIds;
    }

    public List<Integer> getSecondaryColorIds() {
        return secondaryColorIds;
    }

    public List<Integer> getHabitatIds() {
        return habitatIds;
    }

    public List<Integer> getLocationIds() {
        return locationIds;
    }

    public int getWingShapeId() {
        return wingShapeId;
    }

    public int getBillShapeId() {
        return billShapeId;
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
    public boolean equals(Object other) {
        if (!(other instanceof Bird)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return ((Bird) other).getBirdId() == birdId;
    }

    @Override
    public int hashCode() {
        return birdId;
    }

    private List<BirdName> setNames() throws SQLException {
        return BirdNamesRetriever.getAliasList(birdId);
    }

    private List<Integer> setPrimaryColorIds() throws SQLException {
        return ColorsRetriever.getPrimaryColorIds(birdId);
    }

    private List<Integer> setSecondaryColorIds() throws SQLException {
        return ColorsRetriever.getSecondaryColorIds(birdId);
    }

    private List<Integer> setHabitatIds() throws SQLException {
        return HabitatsRetriever.getHabitatIds(birdId);
    }

    private List<Integer> setLocationIds() throws SQLException {
        return LocationRetriever.getLocationIds(birdId);
    }
}
