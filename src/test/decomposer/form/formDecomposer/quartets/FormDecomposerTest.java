package decomposer.form.formDecomposer.quartets;

import decomposer.form.FormDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;
import utils.ModelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class FormDecomposerTest extends AbstractSpringTest {

	@Autowired
	protected FormDecomposer formDecomposer;
	@Autowired
	protected CompositionLoader compositionLoader;

	@Test
	public void quartetScarecrowIntro() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" ) );

		List< List< Melody > > melodyBlockList = formDecomposer.decompose( composition, JMC.WHOLE_NOTE );
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
