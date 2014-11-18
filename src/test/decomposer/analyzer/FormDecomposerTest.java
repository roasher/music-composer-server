package decomposer.analyzer;

import controller.Controller;
import decomposer.FormDecomposer;
import jm.JMC;
import model.Form;
import model.composition.Composition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class FormDecomposerTest {

	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.configuration.xml");

	@Test
	public void testCase1() {
		Controller controller = new Controller();
		Composition composition = controller.getComposition( new File( "src\\test\\decomposer\\analyzer\\form\\testCases\\AABC1.mid" ) );

		FormDecomposer formDecomposer = applicationContext.getBean( FormDecomposer.class );
		List<Form> formList = formDecomposer.getForm( composition, JMC.WHOLE_NOTE );

		assertEquals( formList.get(0).getPart(), 0 );
		assertEquals( formList.get(1).getPart(), 0 );
		assertEquals( formList.get(2).getPart(), 1 );
		assertEquals( formList.get(3).getPart(), 2 );
//		View.notate( score );
//		Utils.suspend();
	}

	@Test
	public void testCase2() {
		Controller controller = new Controller();
		Composition composition = controller.getComposition( new File( "src\\test\\decomposer\\analyzer\\form\\testCases\\ABAB1.mid" ) );

		FormDecomposer formDecomposer = applicationContext.getBean( FormDecomposer.class );
		List<Form> formList = formDecomposer.getForm( composition, JMC.WHOLE_NOTE );

		assertEquals( formList.get(0).getPart(), 0 );
		assertEquals( formList.get(1).getPart(), 1 );
		assertEquals( formList.get(2).getPart(), 0 );
		assertEquals( formList.get(3).getPart(), 1 );
		//		View.notate( score );
		//		Utils.suspend();
	}

	@Test
	public void testCase3() {
		Controller controller = new Controller();
		Composition composition = controller.getComposition( new File( "src\\test\\decomposer\\analyzer\\form\\testCases\\ABAC1.mid" ) );

		FormDecomposer formDecomposer = applicationContext.getBean( FormDecomposer.class );
		List<Form> formList = formDecomposer.getForm( composition, JMC.WHOLE_NOTE );

		assertEquals( formList.get(0).getPart(), 0 );
		assertEquals( formList.get(1).getPart(), 1 );
		assertEquals( formList.get(2).getPart(), 0 );
		assertEquals( formList.get(3).getPart(), 2 );
		//		View.notate( score );
		//		Utils.suspend();
	}
}
