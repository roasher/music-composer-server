package decomposer.analyzer.melody;

import static jm.constants.Pitches.*;

import jm.music.data.Note;
import junit.framework.Assert;
import model.Melody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by night wish on 26.07.14.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = "classpath:spring.configuration.xml" )
public class MelodyEqualityAnalyzerImplTest {

	@Autowired
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
		  new Note( 76, 0.49999999999999906 ),
		  new Note( 74, 0.49999999999999906 ),
		  new Note( 71, 0.49999999999999906 ),
		  new Note( 69, 0.24999999999999906 ),
		  new Note( 67, 0.24999999999999906 ),
		  new Note( 69, 1.05 ),
		  new Note( 71, 0.24999999999999906 ),
		};
		Melody melody2 = new Melody( notes2 );

		Assert.assertFalse( melodyEqualityAnalyzer.isEqual( melody1, melody2 ) );
	}

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

		Assert.assertTrue( melodyEqualityAnalyzer.isEqual( melody1, melody2 ) );
	}
}
