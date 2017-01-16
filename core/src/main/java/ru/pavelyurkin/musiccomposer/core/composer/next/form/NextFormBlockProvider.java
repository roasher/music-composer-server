package ru.pavelyurkin.musiccomposer.core.composer.next.form;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Parent class getting Next Block regarding form
 */
public abstract class NextFormBlockProvider {

    public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps, List<FormCompositionStep> differentFormSteps,
            double length ) {

        CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
        List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
        possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );

        // Leaving all blocks adding whose will keep whole piece in range
        double previouslyComposedRhythmValue = previousCompositionSteps.stream().skip( 1 ).mapToDouble( value -> value.getOriginComposeBlock().getRhythmValue() ).sum();
        List<ComposeBlock> blocksToChooseFrom = possibleNextComposeBlocks.stream().filter( composeBlock -> previouslyComposedRhythmValue + composeBlock.getRhythmValue() <= length )
                .collect( Collectors.toList() );

        return getNextBlock( previousCompositionSteps, similarFormSteps, differentFormSteps, length, blocksToChooseFrom );

    }

    public abstract Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
            List<FormCompositionStep> differentFormSteps, double length, List<ComposeBlock> blocksToChooseFrom );

}
