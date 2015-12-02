package persistance.prototype;

import helper.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import persistance.prototype.ObjectDAO;
import persistance.prototype.Vagon;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by night wish on 01.06.2015.
 */
public class VagonTest extends AbstractSpringTest {

	@Autowired ObjectDAO vagonDAO;

	@Test
	public void test() {
		Vagon vagon1 = new Vagon( 1, "vagon1" );
		Vagon vagon2 = new Vagon( 2, "vagon2" );
		Vagon vagon3 = new Vagon( 3, "vagon3" );
		Vagon vagon4 = new Vagon( 4, "vagon4" );
		Vagon vagon5 = new Vagon( 5, "vagon5" );

		vagon3.possiblePreviousVagons.add( vagon1 );
		vagon1.possibleNextVagons.add( vagon3 );

		vagon3.possiblePreviousVagons.add( vagon2 );
		vagon2.possibleNextVagons.add( vagon3 );

		vagon3.possibleNextVagons.add( vagon4 );
		vagon4.possiblePreviousVagons.add( vagon3 );

		vagon3.possibleNextVagons.add( vagon5 );
		vagon5.possiblePreviousVagons.add( vagon3 );

		List<Vagon> vagonWheels = new ArrayList<>(  );
		vagonWheels.add( vagon1 );
		vagonWheels.add( vagon2 );
		vagonWheels.add( vagon3 );
		vagonWheels.add( vagon4 );
		vagonWheels.add( vagon5 );

		vagonDAO.persist( vagonWheels );
		List<Object> vagonWheelsFromDB = vagonDAO.getAll();

		assertEquals( vagonWheels.size(), vagonWheelsFromDB.size() );

		for ( Object o : vagonWheelsFromDB ) {
			Vagon vagonFromDB = ( Vagon ) o;
			for ( Vagon vagon : vagonWheels ) {
				if ( vagon.name.equals( vagonFromDB.name ) ) {
					assertEquals( vagon.possibleNextVagons.size(), vagonFromDB.possibleNextVagons.size() );
					assertEquals( vagon.possibleNextVagons.size(), vagonFromDB.possibleNextVagons.size() );
				}
			}
		}

	}
}
