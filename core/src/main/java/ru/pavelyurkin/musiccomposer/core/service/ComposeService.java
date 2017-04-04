package ru.pavelyurkin.musiccomposer.core.service;

import javafx.util.Pair;
import jm.JMC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProviderImpl;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class provides different composing services
 */
@Component
public class ComposeService implements ApplicationContextAware {

	private Logger logger  = LoggerFactory.getLogger( getClass() );

	/**
	 * Map, holding parameters to compose per id (session for example)
	 */
	private Map<String , ComposingParameters> composingParametersMap = new HashMap<>();
	/**
	 * Default parameters with lazy init
	 */
	private Lexicon defaultLexicon;
	private ComposeStepProvider defaultComposeStepProvider;

	@Autowired
	private CompositionComposer compositionComposer;
	@Autowired
	private CompositionDecomposer compositionDecomposer;
	@Autowired
	private CompositionLoader compositionLoader;

	@Value( "${Bach.chorale.path:}" )
	private String bachChoralPath;

	private ApplicationContext applicationContext;

	public Composition getNextBarsFromComposition( String compositionId, int numberOfBars ) {
		logger.info( "Getting next {} bars of composition for id = {}", numberOfBars, compositionId );
		ComposingParameters composingParameters;
		if ( composingParametersMap.containsKey( compositionId ) ) {
			composingParameters = composingParametersMap.get( compositionId );
		} else {
			composingParameters = getDefaultComposingParameters();
			composingParametersMap.put( compositionId, composingParameters );
		}
		Pair<Composition, List<CompositionStep>> compose = compositionComposer
				.compose( composingParameters.getComposeStepProvider(), composingParameters.getLexicon(), numberOfBars * JMC.WHOLE_NOTE,
						composingParameters.getPreviousCompositionSteps() );
		if ( compose.getValue() != null ) composingParameters.setPreviousCompositionSteps( compose.getValue() );
		return compose.getKey();
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

	public ComposingParameters getComposingParameters( String id ) {
		return composingParametersMap.get( id );
	}

	public void setDefaultLexicon( Lexicon defaultLexicon ) {
		this.defaultLexicon = defaultLexicon;
	}

	public void setDefaultComposeStepProvider( ComposeStepProvider defaultComposeStepProvider ) {
		this.defaultComposeStepProvider = defaultComposeStepProvider;
	}
}
