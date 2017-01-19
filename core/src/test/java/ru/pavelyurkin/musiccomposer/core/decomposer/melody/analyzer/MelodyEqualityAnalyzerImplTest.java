package ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import jm.music.data.Note;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import static jm.constants.Pitches.REST;

/**
 * Created by night wish on 26.07.14.
 */
public class MelodyEqualityAnalyzerImplTest extends AbstractSpringTest {

	@Autowired @Qualifier("melodyEqualityAnalyzerImpl")
	MelodyEqualityAnalyzer melodyEqualityAnalyzer;

	@Test
	public void testCase1() {
		Note[] notes1 = new Note[] {
		  new Note( 69, 0.5 ),
		  new Note( 67, 0.5 ),
		  new Note( 64, 0.5 ),
		  new Note( 62, 1.5 ),
		  new Note( 60, 0.25 ),
		  new Note( 62, 0.25 ),
		  new Note( 64, 2 ),
		};
		Melody melody1 = new Melody( notes1 );

		Note[] notes2 = new Note[] {
		  new Note( 76, 0.5 ),
		  new Note( 74, 0.5 ),
		  new Note( 71, 0.5 ),
		  new Note( 69, 0.25 ),
		  new Note( 67, 0.25 ),
		  new Note( 69, 1.05 ),
		  new Note( 71, 0.25 ),
		};
		Melody melody2 = new Melody( notes2 );

		Assert.assertFalse( melodyEqualityAnalyzer.isEqual( melody1, melody2 ) );
	}

	@Ignore
	@Test
	public void testCase2() {
		Note[] notes1 = new Note[] {
		  new Note( 69, 0.5 ),
		  new Note( 76, 0.5 ),
		  new Note( 72, 1.5 ),
		  new Note( REST, 1. ),
		  new Note( 72, 0.25 ),
		  new Note( 74, 0.25 ),
		  new Note( 76, 0.25 ),
		  new Note( 77, 0.25 ),
		  new Note( REST, 1 ),
		  new Note( 69, 0.5 ),
		};
		Melody melody1 = new Melody( notes1 );

		Note[] notes2 = new Note[] {
		  new Note( 69, 0.5 ),
		  new Note( 74, 0.5 ),
		  new Note( 72, 1.5 ),
		  new Note( REST, 1. ),
		  new Note( 72, 0.25 ),
		  new Note( 74, 0.25 ),
		  new Note( 76, 0.25 ),
		  new Note( 77, 0.25 ),
		  new Note( REST, 1 ),
		  new Note( 69, 0.5 ),
		};
		Melody melody2 = new Melody( notes2 );

//		View.notate( melody1 );
//		View.notate( melody2 );
//		suspend();

		Assert.assertTrue( melodyEqualityAnalyzer.isEqual( melody1, melody2 ) );
	}
}
