package decomposer.form.analyzer;

import model.melody.Form;
import model.melody.Melody;
import model.MusicBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by pyurkin on 12.11.14.
 */
public class MusicBlockFormEqualityAnalyser {

	private double instrumentEqualityPassThreshold;
	@Autowired
	private MelodyFormEqualityAnalyzer formEqualityAnalyzer;

	Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Consider if music blocks are form equal.
	 * Music blocks considered form equal if enough of their instrument parts is equal.
	 * @param firstMusicBlock
	 * @param secondMusicBlock
	 * @return
	 */
	public boolean isEqual( MusicBlock firstMusicBlock, MusicBlock secondMusicBlock ) {
		if ( firstMusicBlock.getMelodyList().size() != secondMusicBlock.getMelodyList().size() ) {
			logger.info( "Music Blocks has different number of instruments so they can't be considered equal" );
			return false;
		}

		List<Melody> firstMusicBlockInstrumentParts = firstMusicBlock.getMelodyList();
		List<Melody> secondMusicBlockInstrumentParts = secondMusicBlock.getMelodyList();
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

	public boolean isEqualToAnyMusicBlock( MusicBlock inputMusicBlock, List<MusicBlock> musicBlocks ) {
		for ( MusicBlock musicBlock : musicBlocks ) {
			if ( isEqual( inputMusicBlock, musicBlock ) == true ) {
				return true;
			}
		}
		return false;
	}

	public double getInstrumentEqualityPassThreshold() {
		return instrumentEqualityPassThreshold;
	}

	public void setInstrumentEqualityPassThreshold( double instrumentEqualityPassThreshold ) {
		this.instrumentEqualityPassThreshold = instrumentEqualityPassThreshold;
	}
}
