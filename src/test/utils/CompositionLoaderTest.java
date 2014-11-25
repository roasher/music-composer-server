package utils;

import model.composition.Composition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static junit.framework.Assert.assertEquals;

/**
 * Created by pyurkin on 25.11.14.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = "classpath:spring.configuration.xml" )
public class CompositionLoaderTest {

	@Autowired
	private CompositionLoader compositionLoader;

	@Test
	public void correctInstrumentNumberTest() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\quartets\\2.Scarecrow's song (midi).mid" ) );
		assertEquals( composition.getPartArray().length, 4 );
	}
}
