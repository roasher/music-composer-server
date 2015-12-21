package persistance.dao;

import model.Lexicon;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by night wish on 09.05.2015.
 */
@Component
public class LexiconDAO_stub implements LexiconDAO {
	@Override public void persist( Lexicon lexicon ) throws IOException {
		// doing nothing
	}

	@Override public Lexicon fetch() {
		return Lexicon.getBlankLexicon();
	}
}
