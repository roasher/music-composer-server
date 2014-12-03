package decomposer.melody.analyzer;

import model.melody.Melody;

/**
 * Created by Pavel Yurkin on 08.08.14.
 */
public interface MelodyEqualityAnalyzer {
    public boolean isEqual( Melody firstMelody, Melody secondMelody );
}
