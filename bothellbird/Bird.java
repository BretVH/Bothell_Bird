package bothell_bird;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.ImageIcon;

//TODO: implement Comparable, override equals, hashCode, toString
public class Bird {
    private final int birdId;
    private final String scientificName;   
    private final int conservationStatusId;
    private final int familyNameId;
    private final int sizeId;
    private final List<BirdName> names;
    private final List<Integer> primaryColors;
    private final List<Integer> secondaryColors;
    private final List<Feature> habitats;
    private final List<Feature> locations;
    private final ImageIcon icon;
    
    public Bird(int birdId, String name, int cs, int fn, int s) throws SQLException, IOException {       
        this.birdId = birdId;
        this.scientificName = name;
        this.conservationStatusId = cs;
        this.familyNameId = fn;
        this.sizeId = s;
        this.names = setNames();
        this.primaryColors = setPrimaryColors();
        this.secondaryColors = setSecondaryColors();
        //by conventrion let the nameId of the scientific name be 0
        this.icon = ImageRetriever.readData(birdId, 0);
        this.habitats = setHabitats();
        this.locations = setLocations();
    }

    public int getSizeId() {
        return sizeId;
    }

    public int getBirdId() {
        return birdId;
    }

    public String getName() {
        return scientificName;
    }
    
    public int getFamilyNameId() {
        return familyNameId;
    }
    
    public int getConservationStatusId() {
        return conservationStatusId;
    }
    
    public ImageIcon getIcon() {
        return icon;
    }
    
    public BirdName getNeutralName()  {
        for(BirdName name : names) {
            if(name.getGender() == 'n') {
                return name;
            }
        }
        return new BirdName('n', scientificName, 0, birdId);
    }
    
    public List<BirdName> getNames() {
        return names;
    }
    
    public List<Integer> getPrimaryColors() {
        return primaryColors;
    }
    
    public List<Integer> getSecondaryColors() {
        return secondaryColors;
    }
    
    public List<Feature> getHabitats() {
        return habitats;
    }
    
    public List<Feature> getLocations() {
        return locations;
    }
    
    private List<BirdName> setNames() throws SQLException {
        return BirdNamesRetriever.getAliasList(birdId);
    }
    
    private List<Integer> setPrimaryColors() throws SQLException {
        return ColorsRetriever.getPrimaryColors(birdId);
    }
    
    private List<Integer> setSecondaryColors() throws SQLException {
        return ColorsRetriever.getSecondaryColors(birdId);
    }

    private List<Feature> setHabitats() {
        return HabitatsRetriever.getHabitats();
    }

    private List<Feature> setLocations() {
        return LocationRetriever.getLocations();
    }
}
