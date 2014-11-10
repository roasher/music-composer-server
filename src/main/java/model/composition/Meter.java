package model.composition;

/**
 * Created by night wish on 27.07.14.
 */
public class Meter {
    private int numerator;
    private int denominator;

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        Meter meter = ( Meter ) o;

        if ( denominator != meter.denominator ) return false;
        if ( numerator != meter.numerator ) return false;

        return true;
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator( int numerator ) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator( int denominator ) {
        this.denominator = denominator;
    }
}
