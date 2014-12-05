package decomposer;

import decomposer.form.FormDecomposer;
import jm.music.data.Score;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Melody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Class analyses and decomposes the composition, creating MusicBlocks
 * Created by Pavel Yurkin on 21.07.14.
 */
@Component
public class CompositionDecomposer {

	@Autowired
	private FormDecomposer formDecomposer;

    public Set<MusicBlock > decompose( Composition composition, double rhythmValue ) {

        return null;
    }

}
