package decomposer.form;

import controller.Controller;
import decomposer.form.FormDecomposer;
import jm.JMC;
import jm.util.Write;
import model.Form;
import model.composition.Composition;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.CompositionLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

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

		List<Form> formList = formDecomposer.getForm( composition, JMC.WHOLE_NOTE );

		assertEquals( formList.get(0).getPart(), 0 );
		assertEquals( formList.get(1).getPart(), 0 );
		assertEquals( formList.get(2).getPart(), 1 );
		assertEquals( formList.get(3).getPart(), 2 );
//		View.notate( score );
//		Utils.suspend();
	}

	@Test
	public void simpleMelodyABAB1() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\simpleMelodies\\ABAB1.mid" ) );

		List<Form> formList = formDecomposer.getForm( composition, JMC.WHOLE_NOTE );

		assertEquals( formList.get(0).getPart(), 0 );
		assertEquals( formList.get(1).getPart(), 1 );
		assertEquals( formList.get(2).getPart(), 0 );
		assertEquals( formList.get(3).getPart(), 1 );
		//		View.notate( score );
		//		Utils.suspend();
	}

	@Test
	public void simpleMelodyABAC1() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\simpleMelodies\\ABAC1.mid" ) );

		List<Form> formList = formDecomposer.getForm( composition, JMC.WHOLE_NOTE );

		assertEquals( formList.get(0).getPart(), 0 );
		assertEquals( formList.get(1).getPart(), 1 );
		assertEquals( formList.get(2).getPart(), 0 );
		assertEquals( formList.get(3).getPart(), 2 );
		//		View.notate( score );
		//		Utils.suspend();
	}

	@Test
	public void quartetScarecrowIntro() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\quartets\\2.Scarecrow's song (midi).mid" ) );

		List<Form> formList = formDecomposer.getForm( composition, JMC.WHOLE_NOTE );

		List<Form> etalonList = new ArrayList<>(  );

		etalonList.add( new Form( 0 ) );
		etalonList.add( new Form( 1 ) );
		etalonList.add( new Form( 0 ) );
		etalonList.add( new Form( 1 ) );
		etalonList.add( new Form( 2 ) );
		etalonList.add( new Form( 3 ) );
		etalonList.add( new Form( 2 ) );
		etalonList.add( new Form( 4 ) );

		etalonList.add( new Form( 5 ) );
		etalonList.add( new Form( 6 ) );
		etalonList.add( new Form( 5 ) );
		etalonList.add( new Form( 6 ) );
		etalonList.add( new Form( 7 ) );
		etalonList.add( new Form( 8 ) );
		etalonList.add( new Form( 7 ) );
		etalonList.add( new Form( 9 ) );

		etalonList.add( new Form( 10 ) );
		etalonList.add( new Form( 11 ) );
		etalonList.add( new Form( 10 ) );
		etalonList.add( new Form( 11 ) );
		etalonList.add( new Form( 12 ) );
		etalonList.add( new Form( 13 ) );
		etalonList.add( new Form( 12 ) );
		etalonList.add( new Form( 14 ) );

		for ( int currentFormNumber = 0; currentFormNumber < etalonList.size(); currentFormNumber ++ ) {
			System.out.println( etalonList.get( currentFormNumber ).getPart() + "\t" + formList.get( currentFormNumber ).getPart() );
			assertEquals( etalonList.get( currentFormNumber ), formList.get( currentFormNumber ) );
		}
	}
}
