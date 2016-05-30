package bothell_bird;

public class BirdInfo {

    private final String name;
    private final int nameId;
    private final int birdId;

    public BirdInfo(String name, int nameId, int birdId) {
        this.name = name;
        this.birdId = birdId;
        this.nameId = nameId;
    }

    public int getNameId() {
        return nameId;
    }

    public int getBirdId() {
        return birdId;
    }

    public String getName() {
        return name;
    }
}
