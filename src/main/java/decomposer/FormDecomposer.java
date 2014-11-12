package decomposer;

import decomposer.analyzer.form.FormAnalyzer;
import jm.JMC;
import jm.music.data.Score;
import model.Form;
import model.MusicBlock;
import model.utils.ScoreSlicer;
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
	private ScoreSlicer scoreSlicer;
	@Autowired
	private FormAnalyzer formAnalyzer;

	private Logger logger = LoggerFactory.getLogger( getClass() );

	public List<MusicBlock> decompose( Score score ) {
		List<MusicBlock> musicBlocks = scoreSlicer.slice( score, JMC.WHOLE_NOTE );
		for ( int musicBlockNumber = 0; musicBlockNumber < musicBlocks.size(); musicBlockNumber++ ) {
			MusicBlock musicBlock = musicBlocks.get( musicBlockNumber );
			for ( int musicBlockToCompareWithNumber = 0; musicBlockToCompareWithNumber < musicBlockNumber; musicBlockNumber++ ) {
				MusicBlock musicBlockToCompareWith = musicBlocks.get( musicBlockToCompareWithNumber );
				if ( formAnalyzer.equal( musicBlock, musicBlockToCompareWith ) ) {
					musicBlock.setForm( musicBlockToCompareWith.getForm() );
				}
			}
			if ( musicBlock.getForm() == null ) {
				musicBlock.setForm( new Form() );
			}
		}
		return musicBlocks;
	}

	public List<Form> getForm( Score score ) {
		List<Form> formList = new ArrayList<>();
		List<MusicBlock> musicBlockList = decompose( score );
		for ( MusicBlock musicBlock : musicBlockList ) {
			formList.add( musicBlock.getForm() );
		}
	}
}
