package utils;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.util.Read;
import model.composition.Composition;
import model.composition.CompositionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class is responsible for loading midi tracks
 * Created by night wish on 25.11.14.
 */
@Component
public class CompositionLoader {
	@Autowired
	private RhythmValueHandler rhythmValueHandler;

	Logger logger = LoggerFactory.getLogger( getClass() );

	private List<File> listFilesForFolder(final File folder) {
		List<File> nameArray = new ArrayList<>();
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				nameArray.add( fileEntry );
			}
		}
		return nameArray;
	}

	public List < Composition > getCompositionsFromFolder( File directory, List<String> excludePatternList ) {
		List< Composition > compositions = new ArrayList<Composition>(  );
		List< File > listFiles = listFilesForFolder( directory );
		for ( File currentFile : listFiles ) {
			if ( !currentFile.getName().matches( ".*\\.mid" ) ) continue;
			for ( String excludePattern : excludePatternList ) {
				if ( currentFile.getName().matches( excludePattern ) ) continue;
				//".*drum.*"
			}
			Composition composition = getComposition( currentFile );
			compositions.add( composition );
		}
		return compositions;
	}

    public List< Composition > getCompositionsFromFolder( File folder ) {
        return getCompositionsFromFolder( folder, Collections.<String>emptyList() );
    }

	public List< Composition > getCompositions( File ... files) {
		List< Composition > compositions = new ArrayList<>(  );
		for ( File currentDirectory : files) {
			compositions.add( getComposition(currentDirectory) );
		}
		return compositions;
	}

	public Composition getComposition( File file ) {
		logger.info( "Reading composition {}", file );
		CompositionInfo compositionInfo = new CompositionInfo();
		compositionInfo.setTitle( file.getName());

		Composition composition = new Composition();
		composition.setCompositionInfo( compositionInfo );

		Read.midi( composition, file.getAbsolutePath() );
		roundAllRhythmValues( composition );
		return composition;
	}

	public void roundAllRhythmValues( Composition composition ) {
		for ( Part part : composition.getPartArray() ) {
			for ( Phrase phrase : part.getPhraseArray() ) {
				for ( Note note : phrase.getNoteArray() ) {
					double newValue = rhythmValueHandler.roundRhythmValue( note.getRhythmValue() );
					note.setRhythmValue( newValue );
				}
			}
		}
	}

}
