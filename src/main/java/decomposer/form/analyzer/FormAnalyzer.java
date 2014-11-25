package decomposer.form.analyzer;

import model.Melody;
import model.MusicBlock;
import utils.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by pyurkin on 12.11.14.
 */
public class FormAnalyzer {

	private double instrumentEqualityPassThreshold;
	@Autowired
	private FormEqualityAnalyzerImpl formEqualityAnalyzer;

	Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Consider if music blocks are form equal.
	 * Music blocks considered form equal if enough of their instrument parts is equal.
	 * @param firstMusicBlock
	 * @param secondMusicBlock
	 * @return
	 */
	public boolean isEqual( MusicBlock firstMusicBlock, MusicBlock secondMusicBlock ) {
		if ( firstMusicBlock.getNotes().size() != secondMusicBlock.getNotes().size() ) {
			logger.info( "Music Blocks has different number of instruments so they can't be considered equal" );
			return false;
		}

		List<Melody> firstMusicBlockInstrumentParts = ModelUtils.getMelodies( firstMusicBlock );
		List<Melody> secondMusicBlockInstrumentParts = ModelUtils.getMelodies( secondMusicBlock );
		int numberOfEqualInstrumentParts = 0;
		for ( int instrumentPartNumber = 0; instrumentPartNumber < firstMusicBlockInstrumentParts.size(); instrumentPartNumber ++ ) {
			if ( formEqualityAnalyzer.isEqual( firstMusicBlockInstrumentParts.get( instrumentPartNumber ), secondMusicBlockInstrumentParts.get( instrumentPartNumber ) ) ) {
				numberOfEqualInstrumentParts++;
			}
		}

		double successTestPersentage = numberOfEqualInstrumentParts * 1. / firstMusicBlockInstrumentParts.size();
		if ( successTestPersentage >= instrumentEqualityPassThreshold ) {
			logger.info( "Music Blocks considered form - equal" );
			return true;
		} else {
			logger.info( "Successfull tests persentage {} lower than the threshold {}. Music Blocks considered non equal", successTestPersentage, instrumentEqualityPassThreshold );
			return false;
		}
	}

	public double getInstrumentEqualityPassThreshold() {
		return instrumentEqualityPassThreshold;
	}

	public void setInstrumentEqualityPassThreshold( double instrumentEqualityPassThreshold ) {
		this.instrumentEqualityPassThreshold = instrumentEqualityPassThreshold;
	}
}
