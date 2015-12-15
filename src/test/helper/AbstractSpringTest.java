package helper;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.test.FactoryConfigurer;

import java.sql.SQLException;

/**
 * Parent test class
 * Created by pyurkin on 05.12.14.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = "classpath:spring.configuration.xml" )
public abstract class AbstractSpringTest {

	@Autowired
	private FactoryConfigurer factoryConfigurer;


	@Before
	public void reset() throws SQLException {
		factoryConfigurer.resetAllFactories();
	}

}