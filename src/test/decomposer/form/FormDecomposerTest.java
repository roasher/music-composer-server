package decomposer.form;

import jm.JMC;
import model.composition.Composition;
import model.melody.Form;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.CompositionLoader;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = "classpath:spring.configuration.xml" )
public class FormDecomposerTest {

	@Autowired
	private FormDecomposer formDecomposer;
	@Autowired
	private CompositionLoader compositionLoader;

	@Test
	public void simpleMelodyAABC1() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\simpleMelodies\\AABC1.mid" ) );

		List<Form> formList = formDecomposer.getInstrumentForm( 0, composition, JMC.WHOLE_NOTE );

		assertTrue( formList.get( 0 ).getValue() == 'A' );
		assertTrue( formList.get( 1 ).getValue() == 'A' );
		assertTrue( formList.get( 2 ).getValue() == 'B' );
		assertTrue( formList.get( 3 ).getValue() == 'C' );
//		View.notate( score );
//		Utils.suspend();
	}

	@Test
	public void simpleMelodyABAB1() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\simpleMelodies\\ABAB1.mid" ) );

		List<Form> formList = formDecomposer.getInstrumentForm( 0, composition, JMC.WHOLE_NOTE );

		assertTrue( formList.get( 0 ).getValue() == 'A' );
		assertTrue( formList.get( 1 ).getValue() == 'B' );
		assertTrue( formList.get( 2 ).getValue() == 'A' );
		assertTrue( formList.get( 3 ).getValue() == 'B' );
		//		View.notate( score );
		//		Utils.suspend();
	}

	@Test
	public void simpleMelodyABAC1() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\simpleMelodies\\ABAC1.mid" ) );

		List<Form> formList = formDecomposer.getInstrumentForm( 0, composition, JMC.WHOLE_NOTE );

		assertTrue( formList.get( 0 ).getValue() == 'A' );
		assertTrue( formList.get( 1 ).getValue() == 'B' );
		assertTrue( formList.get( 2 ).getValue() == 'A' );
		assertTrue( formList.get( 3 ).getValue() == 'C' );
		//		View.notate( score );
		//		Utils.suspend();
	}

//	@Test
//	public void quartetScarecrowIntro() {
//		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\quartets\\2.Scarecrow's song (midi).mid" ) );
//
//		List<Form> formList = formDecomposer.getInstrumentForm( composition, JMC.WHOLE_NOTE );
//
//		List<Form> etalonList = new ArrayList<>(  );
//
//		etalonList.add( new Form( 0, instrumentParts ) );
//		etalonList.add( new Form( 1, instrumentParts ) );
//		etalonList.add( new Form( 0, instrumentParts ) );
//		etalonList.add( new Form( 1, instrumentParts ) );
//		etalonList.add( new Form( 2, instrumentParts ) );
//		etalonList.add( new Form( 3, instrumentParts ) );
//		etalonList.add( new Form( 2, instrumentParts ) );
//		etalonList.add( new Form( 4, instrumentParts ) );
//
//		etalonList.add( new Form( 5, instrumentParts ) );
//		etalonList.add( new Form( 6, instrumentParts ) );
//		etalonList.add( new Form( 5, instrumentParts ) );
//		etalonList.add( new Form( 6, instrumentParts ) );
//		etalonList.add( new Form( 7, instrumentParts ) );
//		etalonList.add( new Form( 8, instrumentParts ) );
//		etalonList.add( new Form( 7, instrumentParts ) );
//		etalonList.add( new Form( 9, instrumentParts ) );
//
//		etalonList.add( new Form( 10, instrumentParts ) );
//		etalonList.add( new Form( 11, instrumentParts ) );
//		etalonList.add( new Form( 10, instrumentParts ) );
//		etalonList.add( new Form( 11, instrumentParts ) );
//		etalonList.add( new Form( 12, instrumentParts ) );
//		etalonList.add( new Form( 13, instrumentParts ) );
//		etalonList.add( new Form( 12, instrumentParts ) );
//		etalonList.add( new Form( 14, instrumentParts ) );
//
//		for ( int currentFormNumber = 0; currentFormNumber < etalonList.size(); currentFormNumber ++ ) {
//			System.out.println( etalonList.get( currentFormNumber ).getPart() + "\t" + formList.get( currentFormNumber ).getPart() );
//			assertEquals( etalonList.get( currentFormNumber ), formList.get( currentFormNumber ) );
//		}
//	}
}
