package ru.pavelyurkin.musiccomposer.core.service;

import javafx.util.Pair;
import jm.JMC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.next.SimpleNextBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.form.NextFormBlockProviderImpl;
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
	private ComposingParameters defaultComposingParameters;

	@Autowired
	private CompositionComposer compositionComposer;
	@Autowired
	private CompositionDecomposer compositionDecomposer;
	@Autowired
	private CompositionLoader compositionLoader;

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
		Pair<Composition, CompositionStep> compose = compositionComposer
				.compose( composingParameters.getComposeBlockProvider(), composingParameters.getLexicon(), numberOfBars * JMC.WHOLE_NOTE,
						composingParameters.getPreviousCompositionStep() );
		if ( compose.getValue() != null ) composingParameters.setPreviousCompositionStep( compose.getValue() );
		return compose.getKey();
	}

	/**
	 * Returns default Composing Parameters
	 * @return
	 */
	public ComposingParameters getDefaultComposingParameters() {
		if ( defaultComposingParameters == null ) {
			defaultComposingParameters = new ComposingParameters();
			defaultComposingParameters.setComposeBlockProvider( getDefaultComposeBlockProvider() );
			defaultComposingParameters.setLexicon( getDefaultLexicon() );
		}
		return defaultComposingParameters;
	}

	/**
	 * Returns Bach chorale lexicon
	 * @returnd
	 */
	private Lexicon getDefaultLexicon() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "/home/night_wish/Music/Bach chorals cut/" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		return lexicon;
	}

	/**
	 * return Bach chorale compose block provider
	 * @return
	 */
	private ComposeBlockProvider getDefaultComposeBlockProvider() {

		ComposeBlockFilter bachChoralFilter = applicationContext.getBean( BachChoralFilter.class );

		NextFormBlockProviderImpl nextFormBlockProvider = applicationContext.getBean( NextFormBlockProviderImpl.class );
		nextFormBlockProvider.setComposeBlockFilter( bachChoralFilter );

		SimpleNextBlockProvider nextBlockProvider = applicationContext.getBean( SimpleNextBlockProvider.class );
		nextBlockProvider.setComposeBlockFilter( bachChoralFilter );

		ComposeBlockProvider composeBlockProvider = applicationContext.getBean( ComposeBlockProvider.class );
		composeBlockProvider.setNextBlockProvider( nextBlockProvider );
		composeBlockProvider.setNextFormBlockProvider( nextFormBlockProvider );

		return composeBlockProvider;
	}

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ComposingParameters getComposingParameters( String id ) {
		return composingParametersMap.get( id );
	}

	public void setDefaultComposingParameters( ComposingParameters defaultComposingParameters ) {
		this.defaultComposingParameters = defaultComposingParameters;
	}
}
