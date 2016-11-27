package ru.pavelyurkin.musiccomposer.decomposer.form.formDecomposer.simpleMelodies;

import ru.pavelyurkin.musiccomposer.helper.AbstractSpringTest;
import jm.JMC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.decomposer.form.FormDecomposer;
import ru.pavelyurkin.musiccomposer.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.model.melody.Form;
import ru.pavelyurkin.musiccomposer.utils.CompositionLoader;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertTrue;

public class FormDecomposerTest extends AbstractSpringTest {

	@Autowired
	protected FormDecomposer formDecomposer;
	@Autowired
	protected CompositionLoader compositionLoader;

	@Test
	public void simpleMelodyAABC1() {
		Composition composition = compositionLoader.getComposition( new File( this.getClass().getResource( "AABC1.mid" ).getFile() ) );

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
		Composition composition = compositionLoader.getComposition( new File( this.getClass().getResource( "ABAB1.mid" ).getFile() ) );

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
		Composition composition = compositionLoader.getComposition( new File( this.getClass().getResource( "ABAC1.mid" ).getFile() ) );

		List<Form> formList = formDecomposer.getInstrumentForm( 0, composition, JMC.WHOLE_NOTE );

		assertTrue( formList.get( 0 ).getValue() == 'A' );
		assertTrue( formList.get( 1 ).getValue() == 'B' );
		assertTrue( formList.get( 2 ).getValue() == 'A' );
		assertTrue( formList.get( 3 ).getValue() == 'C' );
		//		View.notate( score );
		//		Utils.suspend();
	}
}
