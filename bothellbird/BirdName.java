/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bothell_bird;

/**
 *
 * @author Bret
 */
//TODO: implement Comparable, override equals, hashCode, toString
public class BirdName {

    private final char gender;
    private final String name;
    private final int nameId;
    private final int birdId;

    public BirdName(char gender, String name, int nameId, int birdId) {
        this.gender = gender;
        this.name = name;
        this.nameId = nameId;
        this.birdId = birdId;
    }

    public String getName() {
        return name;
    }

    public char getGender() {
        return gender;
    }

    public int getNameId() {
        return nameId;
    }

    public int getBirdId() {
        return birdId;
    }
}
