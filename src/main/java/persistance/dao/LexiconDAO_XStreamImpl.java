package persistance.dao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.Lexicon;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Class saves the results of work
 * Created by Pavel Yurkin on 01.08.14.
 */
@Component
public class LexiconDAO_XStreamImpl extends AbstractLexiconDAO {

	private File storeFile = getStoreFile();

	private XStream xStream;
	{
		xStream = new XStream( new DomDriver(  ) );
//		xStream.omitField( MusicBlock.class, "next" );
//		xStream.omitField( MusicBlock.class, "previous" );
	}

	@Override public void storeInFile( Lexicon lexicon ) throws IOException {
		String lexiconXML = xStream.toXML( lexicon );
		try( FileWriter fileWriter = new FileWriter( storeFile ) ) {
			fileWriter.write( lexiconXML );
			fileWriter.flush();
		}
	}

	@Override public Lexicon fetch() {
		logger.info( "Fetching lexicon from file: {}", storeFile );
		Lexicon lexicon;
		if ( storeFile.exists() ) {
			lexicon = ( Lexicon ) xStream.fromXML( storeFile );
		} else {
			logger.warn( "file does not exist" );
			lexicon = Lexicon.getBlankLexicon();
		}
		return lexicon;
	}
}
