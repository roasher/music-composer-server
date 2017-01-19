package ru.pavelyurkin.musiccomposer.core.service;

import jm.JMC;
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
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.pavelyurkin.musiccomposer.core.Controller.applicationContext;

/**
 * Class provides different composing services
 */
@Component
public class ComposeService implements ApplicationContextAware {

	/**
	 * Map, holding parameters to compose per id (session for example)
	 */
	private Map<String , ComposingParameters> composingParametersMap = new HashMap<>();

	@Autowired
	private CompositionComposer compositionComposer;
	@Autowired
	private CompositionDecomposer compositionDecomposer;
	@Autowired
	private CompositionLoader compositionLoader;

	private ApplicationContext applicationContext;

	public Composition getNextBarsFromComposition( String compositionId, int numberOfBars ) {
		ComposingParameters composingParameters;
		if ( composingParametersMap.containsKey( compositionId ) ) {
			composingParameters = composingParametersMap.get( compositionId );
		} else {
			composingParameters = getDefaultComposingParameters();
			composingParametersMap.put( compositionId, composingParameters );
		}
		return compositionComposer.compose( composingParameters.getComposeBlockProvider(), composingParameters.getLexicon(), numberOfBars * JMC.WHOLE_NOTE );
	}

	/**
	 * Returns default Composing Parameters
	 * @return
	 */
	public ComposingParameters getDefaultComposingParameters() {
		ComposingParameters composingParameters = new ComposingParameters();
		composingParameters.setComposeBlockProvider( getDefaultComposeBlockProvider() );
		composingParameters.setLexicon( getDefaultLexicon() );
		return composingParameters;
	}

	/**
	 * Returns Bach chorale lexicon
	 * @return
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

		return composeBlockProvider;
	}

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
