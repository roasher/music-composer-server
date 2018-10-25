package ru.pavelyurkin.musiccomposer.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.getTransposePitch;

@Data
@NoArgsConstructor
/**
 * Class represents music MusicBlock Music Block is the cut from the one's partition with some characteristics - the blocks with which new generated composition
 * will be made from. Created by Pavel Yurkin on 18.07.14.
 */
public class MusicBlock implements Serializable {

	// Origin Self Information
	private List<InstrumentPart> instrumentParts;
	private CompositionInfo compositionInfo;
	private Optional<List<Integer>> previousBlockEndPitches;
	private double startTime;

	// Derivative Self Information
	// TODO delete?
	private List<Integer> startIntervalPattern;
	private List<Integer> endIntervalPattern;
	private double rhythmValue;

	public MusicBlock( double startTime, List<InstrumentPart> instrumentParts, CompositionInfo compositionInfo, List<Integer> previousBlockEndPitches ) {
		this( startTime, instrumentParts, compositionInfo );
		this.previousBlockEndPitches = Optional.of( previousBlockEndPitches );
	}

	public MusicBlock( double startTime, List<InstrumentPart> instrumentParts, CompositionInfo inputCompositionInfo ) {
		// check if inputMelody have same rhythm value
		if ( instrumentParts.stream().mapToDouble( InstrumentPart::getRythmValue ).distinct().count() != 1 )
			throw new IllegalArgumentException( "Can't create Music Block: input melody list has melodies with different rhythm value" );
		this.instrumentParts = instrumentParts;
		this.compositionInfo = inputCompositionInfo;
		// Computing derivative information
		{
			// Computing interval patterns
			this.startIntervalPattern = ModelUtils.retrieveFirstIntervalPattern( instrumentParts );
			this.endIntervalPattern = ModelUtils.retrieveLastIntervalPattern( instrumentParts );

			// rhytmValue && start time
			this.rhythmValue = ModelUtils.retrieveRhythmValue( instrumentParts );
			this.startTime = startTime;
		}
		this.previousBlockEndPitches = Optional.empty();
	}

	public MusicBlock( List<MusicBlock> musicBlocks ) {
		long count = musicBlocks.stream()
				.mapToInt( musicBlock -> musicBlock.instrumentParts.size() )
				.distinct()
				.count();
		if (count != 1) throw new RuntimeException( "Music blocks has different part numbers" );

		List<InstrumentPart> instrumentParts = new ArrayList<>();
		for ( int instrumentPartNumber = 0; instrumentPartNumber < musicBlocks.get( 0 ).getInstrumentParts().size(); instrumentPartNumber++ ) {
			instrumentParts.add( new InstrumentPart() );
		}
		double rhythmValue = 0;
		for ( MusicBlock musicBlock : musicBlocks ) {
			for ( int instrumentPartNumber = 0; instrumentPartNumber < musicBlock.getInstrumentParts().size(); instrumentPartNumber++ ) {
				instrumentParts.get( instrumentPartNumber ).add( musicBlock.getInstrumentParts().get( instrumentPartNumber ) );
			}
			rhythmValue += musicBlock.getRhythmValue();
		}

		this.instrumentParts = instrumentParts;
		this.compositionInfo = null;

		this.previousBlockEndPitches = musicBlocks.get( 0 ).getPreviousBlockEndPitches();

		this.startIntervalPattern = musicBlocks.get( 0 ).getStartIntervalPattern();
		this.endIntervalPattern = musicBlocks.get( musicBlocks.size() - 1 ).getEndIntervalPattern();

		this.rhythmValue = rhythmValue;
		this.startTime = musicBlocks.get( 0 ).getStartTime();

	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( int melodyNumber = 0; melodyNumber < this.getInstrumentParts().size(); melodyNumber++ ) {
			stringBuilder.append( " |part " + melodyNumber + ": " );
			InstrumentPart instrumentPart = this.getInstrumentParts().get( melodyNumber );
			this.previousBlockEndPitches.ifPresent( integers -> stringBuilder.append( "[" ).append( integers ).append( "]" ) );
			stringBuilder.append( instrumentPart.toString() );
			stringBuilder.append(" from ");
			stringBuilder.append( this.compositionInfo.getTitle() );
		}
		return stringBuilder.toString();
	}

	//TODO get rid of music block here - should return List<InstrumentPart> only
	public MusicBlock transposeClone( MusicBlock previousBlock ) {
		int transposePitch = getTransposePitch( previousBlock, this );
		List<InstrumentPart> transposedInstrumentParts = this.instrumentParts.stream()
				.map( instrumentPart -> instrumentPart.transposeClone( transposePitch ) )
				.collect( Collectors.toList() );
		MusicBlock transposedBlock = new MusicBlock();
		transposedBlock.setInstrumentParts( transposedInstrumentParts );
		return transposedBlock;
	}

	public MusicBlock clone() {
		MusicBlock clone = new MusicBlock(
				this.startTime,
				this.instrumentParts.stream()
					.map( InstrumentPart::clone )
					.collect( Collectors.toList()),
				this.compositionInfo
		);
		clone.setPreviousBlockEndPitches( this.previousBlockEndPitches );

		return clone;
	}

	public boolean isStartsWithRest() {
		return this.instrumentParts.stream()
				.filter( instrumentPart -> !instrumentPart.startsWithRest() )
				.findAny()
				.isPresent();
	}



	public List<Integer> getEndPitches() {
		return this.instrumentParts.stream()
				.flatMap( instrumentPart -> instrumentPart.getLastVerticalPitches().stream() )
				.collect( Collectors.toList() );
	}

	public boolean isRest() {
		return this.instrumentParts.stream()
				.allMatch( InstrumentPart::isRest );
	}
}
