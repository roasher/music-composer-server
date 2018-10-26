package ru.pavelyurkin.musiccomposer.core.decomposer.form.formDecomposer.quartets;

import jm.JMC;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.decomposer.form.FormDecomposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Ignore
public class FormDecomposerTest extends AbstractSpringTest {

	@Autowired
	protected FormDecomposer formDecomposer;
	@Autowired
	protected CompositionLoader compositionLoader;

	@Test
	public void quartetScarecrowIntro() {
		Composition composition = compositionLoader.getComposition( new File( "src/test/resources/ru/pavelyurkin/musiccomposer/core/utils/2.Scarecrow's_song(midi).mid" ) );

		List< List<InstrumentPart> > melodyBlockList = formDecomposer.decompose( composition, JMC.WHOLE_NOTE );
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

//		double startTime = 0;
//		for ( int currentMelodyBlockNumber = 0; currentMelodyBlockNumber < etalonList.size(); currentMelodyBlockNumber ++ ) {
//			List< InstrumentPart > melodyBlock = melodyBlockList.get( currentMelodyBlockNumber );
//			System.out.println( etalonList.get( currentMelodyBlockNumber ) + "\t" + getFormString( melodyBlock ) );
//			assertEquals( etalonList.get( currentMelodyBlockNumber ), getFormString( melodyBlock ) );
//
//			double rhythmValue = ModelUtils.sumAllRhytmValues( melodyBlock.get( 0 ) );
//			for ( InstrumentPart melody : melodyBlock ) {
//				assertEquals( melody.getStartTime(), startTime );
//				assertEquals( ModelUtils.sumAllRhytmValues( melody ), rhythmValue );
//			}
//			startTime += rhythmValue;
//		}
	}

	public String getFormString( List< Melody > melodyBlock ) {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( Melody melody : melodyBlock ) {
			stringBuilder.append( melody.getForm().getValue() );
		}
		return stringBuilder.toString();
	}
}
