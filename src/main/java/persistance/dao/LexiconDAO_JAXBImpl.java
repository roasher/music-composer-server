package persistance.dao;

import jm.music.data.Note;
import model.Lexicon;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Component
public class LexiconDAO_JAXBImpl extends AbstractLexiconDAO {

	private JAXBContext context;

	public LexiconDAO_JAXBImpl() throws JAXBException {
		context = JAXBContext.newInstance( Lexicon.class, Note.class );
	}

	@Override
	public void storeInFile( Lexicon lexicon ) throws IOException {
		try {
			Marshaller marshaller = context.createMarshaller();
			try( FileOutputStream fileOutputStream = new FileOutputStream( getStoreFile() ) ) {
				//			JAXB.marshal( lexicon, fileOutputStream );
				marshaller.marshal( lexicon, fileOutputStream );
				fileOutputStream.flush();
			}
		} catch ( JAXBException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override public Lexicon fetch() {
		if ( getStoreFile().exists() ) {
			Lexicon lexicon = JAXB.unmarshal( getStoreFile(), Lexicon.class );
			return lexicon;
		} else {
			return Lexicon.getBlankLexicon();
		}
	}
}
