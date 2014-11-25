package utils;

import jm.util.Read;
import model.composition.Composition;
import model.composition.CompositionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is responsible for loading midi tracks
 * Created by night wish on 25.11.14.
 */
@Component
public class CompositionLoader {

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

	public List<Composition> getCompositions( File directory, List<String> excludePatternList ) {
		List< Composition > compositions = new ArrayList<Composition>(  );
		List<File> listFiles = listFilesForFolder( directory );
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

	public List< Composition > getCompositions( File ... directories ) {
		List< Composition > compositions = new ArrayList<Composition>(  );
		for ( File currentDirectory : directories ) {
			compositions.addAll( getCompositions( currentDirectory ) );
		}
		return compositions;
	}

	public Composition getComposition( File file ) {
		logger.info( " Reading composition {}", file );
		CompositionInfo compositionInfo = new CompositionInfo();
		compositionInfo.setTitle( file.getName());

		Composition composition = new Composition();
		composition.setCompositionInfo( compositionInfo );

		Read.midi( composition, file.getAbsolutePath() );
		composition.roundAllRhythmValues();
		return composition;
	}
}
