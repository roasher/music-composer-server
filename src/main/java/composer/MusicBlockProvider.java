package composer;

import model.BlockMovement;
import model.ComposeBlock;
import model.MusicBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 15.01.15.
 */
@Component
public class MusicBlockProvider {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Return numbers that corresponds to MusicBlocks from input collection that considered to be possible next to MusicBlock
	 * that is in currentMusicBlockNumber position in input collection
	 * @param currentMusicBlockNumber
	 * @param musicBlocks
	 * @return
	 */
	public List<Integer> getAllPossibleNextVariantNumbers( int currentMusicBlockNumber, List<MusicBlock> musicBlocks ) {
		MusicBlock currentMusicBlock = musicBlocks.get( currentMusicBlockNumber );
		logger.debug( "Searching for all possible music block next to {}", currentMusicBlock );
		List<Integer> possibleNext = new ArrayList<>(  );
		if ( currentMusicBlock.getNext() == null ) {
			logger.info( "There is no music block after this one in the original composition." );
		} else {
			possibleNext.add( currentMusicBlockNumber + 1 );
			for ( int musicBlockNumber = 0; musicBlockNumber < musicBlocks.size(); musicBlockNumber++ ) {
				MusicBlock musicBlock = musicBlocks.get( musicBlockNumber );
				if ( currentMusicBlock.getNext() != musicBlock && canSubstitute( currentMusicBlock.getNext(), musicBlock ) ) {
					logger.debug( "Possible next music block has been found: {}", musicBlock );
					possibleNext.add( musicBlockNumber );
				}
			}
		}
		return possibleNext;
	}

	public List<Integer> getAllFirstMusicBlockNumbers( List<MusicBlock> musicBlocks ) {
		logger.debug( "Searching for first music blocks from the musicBlocks" );
		List<Integer> firstMusicBlockNumbers = new ArrayList<>();
		for ( int musicBlockNumber = 0; musicBlockNumber < musicBlocks.size(); musicBlockNumber++ ) {
			if ( musicBlocks.get( musicBlockNumber ).getPrevious() == null ) {
				firstMusicBlockNumbers.add( musicBlockNumber );
			}
		}
		return firstMusicBlockNumbers;
	}

	/**
	 * Answers if origin can be replaced by substitutor
	 * @param originBlock
	 * @param substitutorBlock
	 * @return
	 */
	public boolean canSubstitute( MusicBlock originBlock, MusicBlock substitutorBlock ) {
		return canSubstitute( originBlock.getStartIntervalPattern(), originBlock.getBlockMovementFromPreviousToThis(), originBlock.getStartTime(),
				substitutorBlock.getStartIntervalPattern(), substitutorBlock.getBlockMovementFromPreviousToThis(), substitutorBlock.getStartTime() );
	}

	public boolean canSubstitute( ComposeBlock originBlock, ComposeBlock substitutorBlock ) {
		return canSubstitute( originBlock.getStartIntervalPattern(), originBlock.getBlockMovementFromPreviousToThis(), originBlock.getStartTime(),
				substitutorBlock.getStartIntervalPattern(), substitutorBlock.getBlockMovementFromPreviousToThis(), substitutorBlock.getStartTime() );
	}

	private boolean canSubstitute(
			List<Integer> firstStartIntervalPattern,
			BlockMovement fromPreviousToFirst,
			double firstStartTime,

			List<Integer> secondStartIntervalPattern,
			BlockMovement fromPreviousToSecond,
			double secondStartTime ) {

		//		boolean totalEquality = originBlock.equals( substitutorBlock );
		boolean startIntervalPatternEquality = firstStartIntervalPattern.equals( secondStartIntervalPattern );
		boolean blockMovementEquality;
		if ( fromPreviousToFirst == null && fromPreviousToSecond == null ) {
			blockMovementEquality = true;
		} else if ( fromPreviousToFirst != null && fromPreviousToSecond != null ) {
			blockMovementEquality = fromPreviousToFirst.equals( fromPreviousToSecond );
		} else {
			blockMovementEquality = false;
		}

		boolean correlatingTime = onCorrelatedTime( firstStartTime, secondStartTime );

		//		boolean canSubstitute = !totalEquality && startIntervalPatternEquality && blockMovementEquality && correlatingTime;

		boolean canSubstitute = startIntervalPatternEquality && blockMovementEquality && correlatingTime;
		return canSubstitute;
	}

	/**
	 * Check if two times have equal "strength"
	 * @param firstStartTime
	 * @param secondStartTime
	 * @return
	 */
	private boolean onCorrelatedTime( double firstStartTime, double secondStartTime ) {
		int originStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( firstStartTime );
		int substitutorStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( secondStartTime );
		if ( originStartTimeDecimalPlacesNumber == substitutorStartTimeDecimalPlacesNumber ) {
			return true;
		}
		return false;
	}
}
