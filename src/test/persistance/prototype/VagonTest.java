package persistance.prototype;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import helper.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by night wish on 01.06.2015.
 */
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
public class VagonTest extends AbstractSpringTest {

	@Autowired
	ObjectDAO vagonDAO;

	@Test
	@DatabaseSetup("/VagonTest.xml")
	@ExpectedDatabase( assertionMode= DatabaseAssertionMode.NON_STRICT, value = "/VagonTest.test-result.xml" )
	public void test() {
		Vagon vagon1 = new Vagon( 1, "vagon1" );
		Vagon vagon2 = new Vagon( 2, "vagon2" );
		Vagon vagon3 = new Vagon( 3, "vagon3" );
		Vagon vagon4 = new Vagon( 4, "vagon4" );
		Vagon vagon5 = new Vagon( 5, "vagon5" );

		vagon3.previousVagons.add( vagon1 );
		vagon1.nextVagons.add( vagon3 );

		vagon3.previousVagons.add( vagon2 );
		vagon2.nextVagons.add( vagon3 );

		vagon3.nextVagons.add( vagon4 );
		vagon4.previousVagons.add( vagon3 );

		vagon3.nextVagons.add( vagon5 );
		vagon5.previousVagons.add( vagon3 );

		List<Vagon> vagonWheels = new ArrayList<>(  );
		vagonWheels.add( vagon1 );
		vagonWheels.add( vagon2 );
		vagonWheels.add( vagon3 );
		vagonWheels.add( vagon4 );
		vagonWheels.add( vagon5 );

		vagonDAO.persist( vagonWheels );

	}
}
