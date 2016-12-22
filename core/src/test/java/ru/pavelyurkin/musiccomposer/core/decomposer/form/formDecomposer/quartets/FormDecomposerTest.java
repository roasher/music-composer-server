package ru.pavelyurkin.musiccomposer.core.decomposer.form.formDecomposer.quartets;

import ru.pavelyurkin.musiccomposer.core.decomposer.form.FormDecomposer;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import jm.JMC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class FormDecomposerTest extends AbstractSpringTest {

	@Autowired
	protected FormDecomposer formDecomposer;
	@Autowired
	protected CompositionLoader compositionLoader;

	@Test
	public void quartetScarecrowIntro() {
		Composition composition = compositionLoader.getComposition( new File( "src/test/java/ru/pavelyurkin/musiccomposer/decomposer/form/formDecomposer/quartets/2.Scarecrow's_song(midi).mid" ) );

		List< List<Melody> > melodyBlockList = formDecomposer.decompose( composition, JMC.WHOLE_NOTE );
//		List<MusicBlock> musicBlockList = ModelUtils.simpleWrap( melodies );

		List<String> etalonList = new ArrayList<>(  );

		etalonList.add( "AAAA" );
		etalonList.add( "BBAA" );
		etalonList.add( "AAAA" );
		etalonList.add( "BBAA" );
		etalonList.add( "CBAA" );
		etalonList.add( "BBAA" );
		etalonList.add( "DAAA" );
		etalonList.add( "BCAB" );

		// TODO need to be continued

		double startTime = 0;
		for ( int currentMelodyBlockNumber = 0; currentMelodyBlockNumber < etalonList.size(); currentMelodyBlockNumber ++ ) {
			List< Melody > melodyBlock = melodyBlockList.get( currentMelodyBlockNumber );
			System.out.println( etalonList.get( currentMelodyBlockNumber ) + "\t" + getFormString( melodyBlock ) );
			assertEquals( etalonList.get( currentMelodyBlockNumber ), getFormString( melodyBlock ) );

			double rhythmValue = ModelUtils.sumAllRhytmValues( melodyBlock.get( 0 ) );
			for ( Melody melody : melodyBlock ) {
				assertEquals( melody.getStartTime(), startTime );
				assertEquals( ModelUtils.sumAllRhytmValues( melody ), rhythmValue );
			}
			startTime += rhythmValue;
		}
	}

	public String getFormString( List< Melody > melodyBlock ) {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( Melody melody : melodyBlock ) {
			stringBuilder.append( melody.getForm().getValue() );
		}
		return stringBuilder.toString();
	}
}
