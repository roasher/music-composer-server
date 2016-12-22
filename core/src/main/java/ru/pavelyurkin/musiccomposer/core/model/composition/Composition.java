package ru.pavelyurkin.musiccomposer.core.model.composition;

import jm.music.data.Part;
import jm.music.data.Score;

import java.util.List;

/**
 * Class extends Score class from jMusic adding new information about the Composition
 * Created by night wish on 27.07.14.
 */
public class Composition extends jm.music.data.Score {
    private CompositionInfo compositionInfo;

	public Composition() {}

	public Composition( Score score ) { super( score.getPartArray() ); }

	public Composition( Part[] parts ) { super( parts ); };

	public Composition( List< Part > parts ) { super( parts.toArray( new Part[]{} ) ); }

    public CompositionInfo getCompositionInfo() {
        return compositionInfo;
    }

    public void setCompositionInfo( CompositionInfo compositionInfo ) {
        this.compositionInfo = compositionInfo;
    }

}
