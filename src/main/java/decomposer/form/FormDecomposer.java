package decomposer.form;

import decomposer.form.analyzer.FormAnalyzer;
import model.Form;
import model.MusicBlock;
import model.composition.Composition;
import decomposer.form.slicer.CompositionSlicer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides form analyses
 * Created by night wish on 08.11.14.
 */
@Component
public class FormDecomposer {
	@Autowired
	private CompositionSlicer compositionSlicer;
	@Autowired
	private FormAnalyzer formAnalyzer;

	private Logger logger = LoggerFactory.getLogger( getClass() );

	public List<MusicBlock> decompose( Composition composition, double rhythmValue ) {
		int formCounter = 0;
		List<MusicBlock> musicBlocks = compositionSlicer.slice( composition, rhythmValue );
		for ( int musicBlockNumber = 0; musicBlockNumber < musicBlocks.size(); musicBlockNumber++ ) {
			MusicBlock musicBlock = musicBlocks.get( musicBlockNumber );
			for ( int musicBlockToCompareWithNumber = 0; musicBlockToCompareWithNumber < musicBlockNumber; musicBlockToCompareWithNumber++ ) {
				MusicBlock musicBlockToCompareWith = musicBlocks.get( musicBlockToCompareWithNumber );
				if ( formAnalyzer.isEqual( musicBlock, musicBlockToCompareWith ) ) {
					musicBlock.setForm( musicBlockToCompareWith.getForm() );
				}
			}
			if ( musicBlock.getForm() == null ) {
				musicBlock.setForm( new Form( formCounter++ ) );
			}
		}
		return musicBlocks;
	}

	public List<Form> getForm( Composition composition, double rhythmValue ) {
		List<Form> formList = new ArrayList<>();
		List<MusicBlock> musicBlockList = decompose( composition, rhythmValue );
		for ( MusicBlock musicBlock : musicBlockList ) {
			formList.add( musicBlock.getForm() );
		}
		return formList;
	}
}
