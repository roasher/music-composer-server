package model.utils;

import junit.framework.TestCase;
import model.utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel Yurkin on 20.07.14.
 */
public class ModelUtilsTest extends TestCase {
    public void testGetIntervalPattern() throws Exception {
        List< Integer > pitches = new ArrayList< Integer >( );
        // 60 62 64 64 70 89 100
        pitches.add( 100 );
        pitches.add( 60 );
        pitches.add( 62 );
        pitches.add( 89 );
        pitches.add( 64 );
        pitches.add( 64 );
        pitches.add( 70 );

        List< Integer > intervalPattern = new ArrayList< Integer >( pitches.size() - 1 );
        // 60 <2> 62 <2> 64 <0> 64 <6> 70 <19> 89 <11> 100
        intervalPattern.add( 2 );
        intervalPattern.add( 2 );
        intervalPattern.add( 0 );
        intervalPattern.add( 6 );
        intervalPattern.add( 19 );
        intervalPattern.add( 11 );

        assertEquals( intervalPattern, ModelUtils.getIntervalPattern( pitches ) );
    }
}
