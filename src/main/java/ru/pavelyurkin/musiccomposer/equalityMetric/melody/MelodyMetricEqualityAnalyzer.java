package ru.pavelyurkin.musiccomposer.equalityMetric.melody;

import ru.pavelyurkin.musiccomposer.equalityMetric.EqualityMetricAnalyzer;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.pavelyurkin.musiccomposer.utils.Recombinator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jm.constants.Durations.WHOLE_NOTE;

@Component
public class MelodyMetricEqualityAnalyzer implements EqualityMetricAnalyzer<Melody> {

	@Override
	public double getEqualityMetric( Melody firstMelody, Melody secondMelody ) {
		if ( Double.compare( firstMelody.getRythmValue(), secondMelody.getRythmValue() ) != 0 ) {
			throw new IllegalArgumentException( "Input melodies have different rhythm values." );
		}
		List<Double> unionRhythmValues = getUnionRhythmValues( firstMelody.getNoteList(), secondMelody.getNoteList() );
		List<Note> transformedFirst = transformMelodyToNewRhythmValues( firstMelody.getNoteList(), unionRhythmValues );
		List<Note> transformedSecond = transformMelodyToNewRhythmValues( secondMelody.getNoteList(), unionRhythmValues );
		Assert.isTrue( transformedFirst.size() == transformedSecond.size() );
		/*
		Calculating rhythm difference metric
		For now simply number of rhythm changes
		 */
		double rhythmDiffMetric = unionRhythmValues.size() - secondMelody.size();
		/*
		Calculating stong/weak time ( changed notes of strong time are more different than others )
		 */
		List<Boolean> isStrongTime = getStrongTimeMetric( unionRhythmValues );

		/*
		Next, we are going to count pitch different metric.
		Taking into consideration that it should be done regardless transposition we will count several metrics and choose min
		We are going to use brute force - counting metric considering transposition of first note of secondMelody
		to first note of the firstMelody, than second to second and so on...
		 */
		double minPitchDiffMetric = Integer.MAX_VALUE;
		for ( int numberOfNoteThatWeAreSettingEqual = 0;
			  numberOfNoteThatWeAreSettingEqual < transformedFirst.size(); numberOfNoteThatWeAreSettingEqual++ ) {
			Note note1 = transformedFirst.get( numberOfNoteThatWeAreSettingEqual );
			Note note2 = transformedSecond.get( numberOfNoteThatWeAreSettingEqual );
			if ( note1.isRest() || note2.isRest() ) {
				continue;
			}
			int pitchToTransposeSecond = note1.getPitch() - note2.getPitch();

			double pitchDiffMetric = 0;
			for ( int currentNote = 0; currentNote < transformedFirst.size(); currentNote++ ) {
				// All logic of calculating pitch diff metric goes here
				int transposedSecondMelodyNotePitch =
						transformedSecond.get( currentNote ).getPitch() + pitchToTransposeSecond;
				int firstMelodyNotePitch = transformedFirst.get( currentNote ).getPitch();
				if ( firstMelodyNotePitch != transposedSecondMelodyNotePitch ) {
					// Different note penalty
					pitchDiffMetric += 1;
					// Penalty for pitch difference
					int diff = Math.abs( firstMelodyNotePitch - transposedSecondMelodyNotePitch );
					pitchDiffMetric += ( 0.2 * diff ) / Note.MAX_PITCH;
					// Penalty for strong time
					if ( isStrongTime.get( currentNote ) ) {
						pitchDiffMetric += 0.2;
					}
				}
			}
			if ( pitchDiffMetric < minPitchDiffMetric ) {
				minPitchDiffMetric = pitchDiffMetric;
			}
		}
		/*
		Now we will combine pitch diff metric with rhythmValue diff metric
		 */
		return 1 - ( rhythmDiffMetric + minPitchDiffMetric ) / secondMelody.size();
	}

	/**
	 * Function returns list of booleans each of them defy if according input rhythm value is on strong time or not
	 * @param rhythmValues
	 * @return
	 */
	List<Boolean> getStrongTimeMetric( List<Double> rhythmValues ) {
		/*
		 implementation logic is the following:
		 searching for rhythmValue how many notes of its value can be placed if front of him:
		 if number is odd then it is a strongTime, weakTime otherwise
		  */
		List<Boolean> out = new ArrayList<>();
		double rhythmValuesSoFar = 0;
		double previousRhythmValueFromBar = 0;
		for ( int rhythmValueNumber = 0; rhythmValueNumber < rhythmValues.size(); rhythmValueNumber++ ) {
			double currentRhythmValue = rhythmValues.get( rhythmValueNumber );
			double valueLeftToFillTheBar = WHOLE_NOTE - rhythmValuesSoFar % WHOLE_NOTE;
			// we should cut rhythm value if it's to big and getting out of the bar line
			double transformed = valueLeftToFillTheBar > currentRhythmValue ? currentRhythmValue : valueLeftToFillTheBar;
			// if rhythm value is not fit into previous rhythm values - we are counting metric as it was previous
			if ( Double.compare( rhythmValuesSoFar % transformed, 0 ) != 0 ) {
				transformed = previousRhythmValueFromBar;
			}
			int numberOfRhythmValuesThatFit = (int) ( rhythmValuesSoFar / transformed );
			out.add( numberOfRhythmValuesThatFit % 2 == 0 );
			rhythmValuesSoFar += currentRhythmValue;
			previousRhythmValueFromBar = currentRhythmValue % WHOLE_NOTE;
		}
		return out;
	}

	/**
	 * Transforms melody to new melody with same pitches but another rhythm values
	 * Asserting that sum of notes rhythm values is equal with sum of newRhythmValues
	 * @param notes input notes
	 * @param newRhythmValues rhythm values that output notes will have
	 * @return
	 */
	private List<Note> transformMelodyToNewRhythmValues( List<Note> notes, List<Double> newRhythmValues ) {
		List<Note> out = new ArrayList<>();
		int notesRhythmValueCounter = 0;
		int newRhythmValuesCounter = 0;
		double sumRhythmValue = 0;
		while ( notesRhythmValueCounter != notes.size() ) {
			Note note = notes.get( notesRhythmValueCounter );
			double rhythmValue = newRhythmValues.get( newRhythmValuesCounter );
			sumRhythmValue += rhythmValue;
			out.add( new Note( note.getPitch(), rhythmValue ) );
			if ( Double.compare( note.getRhythmValue(), sumRhythmValue ) == 0 ) {
				notesRhythmValueCounter++;
				sumRhythmValue = 0;
			}
			newRhythmValuesCounter++;
		}
		return out;
	}

	/**
	 * 	Return "union" of rhythm values
	 *	first: 				---|--|--|-|
	 *	second:				-|---|---|--
	 *	result:				-|-|-||--|-|
	 * @param firstNotes
	 * @param secondNotes
	 * @return
	 */
	private List<Double> getUnionRhythmValues( List<Note> firstNotes, List<Note> secondNotes ) {
		Set<Double> firstEdges = new HashSet<>( Recombinator.getEdgeList( firstNotes ) );
		List<Double> secondEdges = Recombinator.getEdgeList( secondNotes );
		firstEdges.addAll( secondEdges );
		List<Double> unionEdges = firstEdges.stream().sorted().collect( Collectors.toList() );
		List<Double> out = new ArrayList<>();
		for ( int numberOfEdge = 0; numberOfEdge < unionEdges.size(); numberOfEdge++ ) {
			if ( numberOfEdge == 0 ) {
				out.add( unionEdges.get( numberOfEdge ) );
			} else {
				out.add( unionEdges.get( numberOfEdge ) - unionEdges.get( numberOfEdge - 1 ) );
			}
		}
		return out;
	}
}
