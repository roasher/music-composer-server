package helper;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Parent test class
 * Created by pyurkin on 05.12.14.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = "classpath:spring.configuration.xml" )
public abstract class AbstractSpringTest {
}
