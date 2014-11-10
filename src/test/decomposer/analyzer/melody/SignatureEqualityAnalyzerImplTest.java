package decomposer.analyzer.melody;

import decomposer.analyzer.signature.SignatureEqualityAnalyzerImpl;
import static jm.constants.Pitches.*;
import jm.music.data.Note;
import junit.framework.Assert;
import model.Signature;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Class analyzes if two signatures can be considered equal
 * Created by night wish on 26.07.14.
 */
public class SignatureEqualityAnalyzerImplTest {

    ApplicationContext applicationContext = new ClassPathXmlApplicationContext( "spring.configuration.xml" );

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
        Signature signature1 = new Signature( notes1 );

        Note[] notes2 = new Note[] {
                new Note( 76, 0.49999999999999906 ),
                new Note( 74, 0.49999999999999906 ),
                new Note( 71, 0.49999999999999906 ),
                new Note( 69, 0.24999999999999906 ),
                new Note( 67, 0.24999999999999906 ),
                new Note( 69, 1.05 ),
                new Note( 71, 0.24999999999999906 ),
        };
        Signature signature2 = new Signature( notes2 );

        Assert.assertFalse(applicationContext.getBean(SignatureEqualityAnalyzerImpl.class).isEqual(signature1, signature2));
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
        Signature signature1 = new Signature( notes1 );

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
        Signature signature2 = new Signature( notes2 );

        Assert.assertTrue(applicationContext.getBean(SignatureEqualityAnalyzerImpl.class).isEqual(signature1, signature2));
    }
}
