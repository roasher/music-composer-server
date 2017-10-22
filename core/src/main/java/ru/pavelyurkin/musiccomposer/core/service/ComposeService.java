package ru.pavelyurkin.musiccomposer.core.service;

import javafx.util.Pair;
import jm.JMC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.next.FilteredNextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProviderImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.AbstractComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class provides different composing services
 */
@Slf4j
@Component
public class ComposeService implements ApplicationContextAware {

	/**
	 * Map, holding parameters to compose per id (session for example)
	 */
	private Map<String , ComposingParameters> composingParametersMap = new HashMap<>();
	/**
	 * Default parameters with lazy init
	 */
	private Lexicon defaultLexicon;
	private ComposeStepProvider defaultComposeStepProvider;

	private final CompositionComposer compositionComposer;
	private final CompositionDecomposer compositionDecomposer;
	private final CompositionLoader compositionLoader;

	@Value( "${Bach.chorale.path:}" )
	private String bachChoralPath;

	private ApplicationContext applicationContext;

	@Autowired
	public ComposeService( CompositionComposer compositionComposer, CompositionDecomposer compositionDecomposer, CompositionLoader compositionLoader ) {
		this.compositionComposer = compositionComposer;
		this.compositionDecomposer = compositionDecomposer;
		this.compositionLoader = compositionLoader;
	}

	public CompositionFrontDTO getNextBarsFromComposition( String compositionId, int numberOfBars, List<AbstractComposeStepFilter> composeStepFiltersToReplace ) {
		log.info( "Getting next {} bars of composition for id = {}", numberOfBars, compositionId );
		ComposingParameters composingParameters = getComposingParameters( compositionId );
		ComposeStepProvider composeStepProvider = composingParameters.getComposeStepProvider();
		replaceFilters( composeStepProvider, composeStepFiltersToReplace );
		double previousSumRhythmValue = getPreviousSumRhythmValue( composingParameters.getPreviousCompositionSteps() );
		Pair<Composition, List<CompositionStep>> compose = compositionComposer
				.compose( composeStepProvider, composingParameters.getLexicon(), numberOfBars * JMC.WHOLE_NOTE,
						composingParameters.getPreviousCompositionSteps() );
		if ( compose.getValue() != null ) composingParameters.setPreviousCompositionSteps( compose.getValue() );
		return new CompositionFrontDTO( compose.getKey(), previousSumRhythmValue );
	}

	private ComposingParameters getComposingParameters( String compositionId ) {
		ComposingParameters composingParameters;
		if ( composingParametersMap.containsKey( compositionId ) ) {
			composingParameters = composingParametersMap.get( compositionId );
		} else {
			composingParameters = getDefaultComposingParameters();
			composingParametersMap.put( compositionId, composingParameters );
		}
		return composingParameters;
	}

	private void replaceFilters( ComposeStepProvider composeStepProvider,
			List<AbstractComposeStepFilter> composeStepFiltersToReplace ) {
		if ( composeStepFiltersToReplace == null || composeStepFiltersToReplace.isEmpty() ) return;
		NextStepProvider nextStepProvider = composeStepProvider.getNextStepProvider();
		if ( nextStepProvider instanceof FilteredNextStepProvider ) {
			composeStepFiltersToReplace.forEach( filter -> {
				( ( FilteredNextStepProvider ) nextStepProvider ).getComposeStepFilter().replaceFilter( filter );
			} );
		} else {
			throw new IllegalStateException( "Can't replace filter because provider isn't filtered type" );
		}
	}

	private double getPreviousSumRhythmValue( List<CompositionStep> previousCompositionSteps ) {
		return previousCompositionSteps.stream().mapToDouble( value -> value.getTransposedBlock().getRhythmValue() ).sum();
	}

	/**
	 * Returns default Composing Parameters
	 * @return
	 */
	public ComposingParameters getDefaultComposingParameters() {
		ComposingParameters composingParameters = new ComposingParameters();
		composingParameters.setComposeStepProvider( getDefaultComposeStepProvider() );
		composingParameters.setLexicon( getDefaultLexicon() );
		return composingParameters;
	}

	/**
	 * Returns Bach chorale lexicon
	 * @returnd
	 */
	private Lexicon getDefaultLexicon() {
		if ( defaultLexicon == null ) {
			List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( bachChoralPath ) );
			defaultLexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		}
		return defaultLexicon;
	}

	/**
	 * return Bach chorale compose block provider
	 * @return
	 */
	private ComposeStepProvider getDefaultComposeStepProvider() {
		if ( defaultComposeStepProvider == null ) {
			ComposeStepFilter bachChoralFilter = applicationContext.getBean( BachChoralFilter.class );

			NextStepProviderImpl nextFormBlockProvider = applicationContext.getBean( NextStepProviderImpl.class );
			nextFormBlockProvider.setComposeStepFilter( bachChoralFilter );

			defaultComposeStepProvider = applicationContext.getBean( ComposeStepProvider.class );
			defaultComposeStepProvider.setNextStepProvider( nextFormBlockProvider );
		}
		return defaultComposeStepProvider;
	}

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void loadDefaultLexicon() {
		getDefaultLexicon();
	}

	public ComposingParameters getComposingParametersById( String id ) {
		return composingParametersMap.get( id );
	}

	public void setDefaultLexicon( Lexicon defaultLexicon ) {
		this.defaultLexicon = defaultLexicon;
	}

	public void setDefaultComposeStepProvider( ComposeStepProvider defaultComposeStepProvider ) {
		this.defaultComposeStepProvider = defaultComposeStepProvider;
	}
}
