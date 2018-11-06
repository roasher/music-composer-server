package ru.pavelyurkin.musiccomposer.core.helper;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.pavelyurkin.musiccomposer.core.MyTestConfiguration;

/**
 * Parent test class
 * Created by pyurkin on 05.12.14.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = MyTestConfiguration.class )
@ActiveProfiles( "test" )
public abstract class AbstractSpringTest {

}