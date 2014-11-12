package decomposer.analyzer.melody;

import model.Melody;

/**
 * Created by Pavel Yurkin on 08.08.14.
 */
public interface MelodyEqualityAnalyzer {
    public boolean isEqual( Melody firstMelody, Melody secondMelody );
}
