package ru.pavelyurkin.musiccomposer.composer.next;

import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles logic of getting next step
 */
public abstract class NextBlockProvider {

    public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps, List<FormCompositionStep> differentFormSteps,
            double length ) {

        CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
        List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
        possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );

        // Leving all blocks adding whose will keep whole piece in range
        double previouslyComposedRhythmValue = previousCompositionSteps.stream().skip( 1 ).mapToDouble( value -> value.getOriginComposeBlock().getRhythmValue() ).sum();
        List<ComposeBlock> blocksToChooseFrom = possibleNextComposeBlocks.stream().filter( composeBlock -> previouslyComposedRhythmValue + composeBlock.getRhythmValue() <= length )
                .collect( Collectors.toList() );

        return getNextBlock( previousCompositionSteps, similarFormSteps, differentFormSteps, length, blocksToChooseFrom );

    }

    public abstract Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
            List<FormCompositionStep> differentFormSteps, double length, List<ComposeBlock> blocksToChooseFrom );

}
