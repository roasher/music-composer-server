package database;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.ComposeBlock;
import model.Lexicon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

/**
 * Class saves the results of work
 * Created by Pavel Yurkin on 01.08.14.
 */
@Component
public class LexiconDAO {

	private File storeFile = new File( "src\\main\\java\\database\\Lexicon.xml" );
	private XStream xStream = new XStream(  new DomDriver(  ) );
	Logger logger = LoggerFactory.getLogger( getClass() );

	public void store( Lexicon lexicon ) throws IOException {
		logger.info( "Storing lexicon to file: {}", storeFile );
		String lexiconXML = xStream.toXML( lexicon );
		try( FileWriter fileWriter = new FileWriter( storeFile ) ) {
			fileWriter.write( lexiconXML );
			fileWriter.flush();
		}
	}

	public Lexicon fetch() {
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

	public File getStoreFile() {
		return storeFile;
	}

	public void setStoreFile( File storeFile ) {
		this.storeFile = storeFile;
	}
}
