package decomposer.form.formDecomposer.quartets;

import decomposer.form.FormDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.MusicBlock;
import model.composition.Composition;
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

		List<MusicBlock> musicBlockList = ModelUtils.simpleWrap( formDecomposer.decompose( composition, JMC.WHOLE_NOTE ) );

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

		for ( int currentFormNumber = 0; currentFormNumber < etalonList.size(); currentFormNumber ++ ) {
			System.out.println( etalonList.get( currentFormNumber ) + "\t" + musicBlockList.get( currentFormNumber ).getForm() );
			assertEquals( etalonList.get( currentFormNumber ), musicBlockList.get( currentFormNumber ).getForm() );
		}
	}
}
