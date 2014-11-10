package model;

/**
 * Class represents form of the composition
 * Created by night wish on 18.10.14.
 */
public class Form {
    // As for now form will have only one metric - integer number describes part
    private int part;

    public Form( int part ) { this.part = part; }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }
}
