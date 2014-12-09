package model.composition;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import utils.Utils;

/**
 * Class extends Score class from jMusic adding new information about the Composition
 * Created by night wish on 27.07.14.
 */
public class Composition extends jm.music.data.Score {
    private CompositionInfo compositionInfo;

	public Composition() {}

	public Composition( Score score ) { super( score.getPartArray() ); }

    public CompositionInfo getCompositionInfo() {
        return compositionInfo;
    }

    public void setCompositionInfo( CompositionInfo compositionInfo ) {
        this.compositionInfo = compositionInfo;
    }
}
