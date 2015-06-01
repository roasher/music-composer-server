package persistance;

import helper.AbstractSpringTest;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import persistance.test.ObjectDAO;
import persistance.test.Vagon;
import persistance.test.VagonCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by night wish on 01.06.2015.
 */
public class ManyToManyTest extends AbstractSpringTest {

	@Autowired ObjectDAO vagonDAO;

	@Test
	public void test() {
		Vagon vagon1 = new Vagon( "vagon1" );
		Vagon vagon2 = new Vagon( "vagon2" );
		Vagon vagon3 = new Vagon( "vagon3" );
		Vagon vagon4 = new Vagon( "vagon4" );
		Vagon vagon5 = new Vagon( "vagon5" );

		vagon3.possiblePreviousVagons.add( vagon1 ); vagon1.possibleNextVagons.add( vagon3 );
		vagon3.possiblePreviousVagons.add( vagon2 ); vagon2.possibleNextVagons.add( vagon3 );

		vagon3.possibleNextVagons.add( vagon4 ); vagon4.possiblePreviousVagons.add( vagon3 );
		vagon3.possibleNextVagons.add( vagon5 ); vagon5.possiblePreviousVagons.add( vagon3 );

//		vagonDAO.persist( vagon1 );
//		vagonDAO.persist( vagon2 );
//		vagonDAO.persist( vagon3 );
//		vagonDAO.persist( vagon4 );
//		vagonDAO.persist( vagon5 );

//		List<Object> vagonWheels = vagonDAO.getAll();

		List<Vagon> vagonWheels = new ArrayList<>(  );
		vagonWheels.add( vagon1 );
		vagonWheels.add( vagon2 );
		vagonWheels.add( vagon3 );
		vagonWheels.add( vagon4 );
		vagonWheels.add( vagon5 );

		vagonDAO.persist( vagonWheels );

		System.out.print( vagonWheels );
	}
}
