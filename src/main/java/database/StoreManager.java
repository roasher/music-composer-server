package database;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.Lexicon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Class saves the results of work
 * Created by Pavel Yurkin on 01.08.14.
 */
public class StoreManager {

	private String storeFile = "src\\main\\java\\database\\Lexicon.xml";
	private XStream xStream = new XStream(  new DomDriver(  ) );
	Logger logger = LoggerFactory.getLogger( getClass() );

	public void store( Lexicon lexicon ) throws IOException {
		logger.info( "Storing lexicon to file: {}", storeFile );
		String lexiconXML = xStream.toXML( lexicon );
		FileWriter fileWriter = new FileWriter( storeFile );
		fileWriter.write( lexiconXML );
	}

	public Lexicon fetch() {
		logger.info( "Fetching lexicon from file: {}", storeFile );
		Lexicon lexicon = ( Lexicon ) xStream.fromXML( storeFile );
		return lexicon;
	}

}
