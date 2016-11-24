package ru.pavelyurkin.musiccomposer.model;

import ru.pavelyurkin.musiccomposer.model.composition.CompositionInfo;

import static ru.pavelyurkin.musiccomposer.utils.Utils.isEquals;

/**
 * Created by night wish on 27.07.14.
 */
public class PlaceInTheComposition {
    private CompositionInfo compositionInfo;
    private double firstNoteStartTime;
    private double lastNoteEndTime;

    public PlaceInTheComposition( CompositionInfo compositionInfo, double firstNoteStartTime, double lastNoteEndTime ) {
        this.compositionInfo = compositionInfo;
        this.firstNoteStartTime = firstNoteStartTime;
        this.lastNoteEndTime = lastNoteEndTime;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        PlaceInTheComposition that = ( PlaceInTheComposition ) o;

        if ( !isEquals( that.firstNoteStartTime, firstNoteStartTime ) ) return false;
        if ( !isEquals( that.lastNoteEndTime, lastNoteEndTime ) ) return false;
        if ( compositionInfo != null ? !compositionInfo.equals( that.compositionInfo ) : that.compositionInfo != null )
            return false;

        return true;
    }

    public CompositionInfo getCompositionInfo() {
        return compositionInfo;
    }

    public void setCompositionInfo( CompositionInfo compositionInfo ) {
        this.compositionInfo = compositionInfo;
    }

    public double getFirstNoteStartTime() {
        return firstNoteStartTime;
    }

    public void setFirstNoteStartTime( double firstNoteStartTime ) {
        this.firstNoteStartTime = firstNoteStartTime;
    }

    public double getLastNoteEndTime() {
        return lastNoteEndTime;
    }

    public void setLastNoteEndTime( double lastNoteEndTime ) {
        this.lastNoteEndTime = lastNoteEndTime;
    }
}
