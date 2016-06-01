package bothell_bird;

public class Bird {
    private final int birdId;
    private final String scientificName;   
    private final int conservationStatusId;
    private final int familyNameId;
    private final int sizeId;

    public Bird(int birdId, String name, int cs, int fn, int s) {       
        this.birdId = birdId;
        this.scientificName = name;
        this.conservationStatusId = cs;
        this.familyNameId = fn;
        this.sizeId = s;
    }

    public int sizeId() {
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
}
