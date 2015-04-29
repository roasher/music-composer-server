package persistance.dao;

import model.Lexicon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by pyurkin on 29.04.2015.
 */
public abstract class AbstractLexiconDAO implements LexiconDAO {

	private File storeFile = new File( "src\\main\\java\\persistance\\Lexicon.xml" );
	public final Logger logger = LoggerFactory.getLogger( getClass() );

	@Override
	public void store( Lexicon lexicon ) throws IOException {
		logger.info( "Storing lexicon to file: {}", storeFile );
		if ( !storeFile.exists() ) {
			storeFile.createNewFile();
		}
		storeInFile( lexicon );
	}

	public abstract void storeInFile( Lexicon lexicon ) throws IOException;

	public File getStoreFile() {
		return storeFile;
	}

	public void setStoreFile( File storeFile ) {
		this.storeFile = storeFile;
	}
}
