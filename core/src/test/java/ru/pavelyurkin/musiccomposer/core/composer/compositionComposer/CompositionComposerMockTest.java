package ru.pavelyurkin.musiccomposer.core.composer.compositionComposer;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import jm.JMC;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.FormBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pyurkin on 15.12.14.
 */
public class CompositionComposerMockTest extends AbstractSpringTest {

	@InjectMocks
	private CompositionComposer compositionComposer;

	@Mock
	private FormBlockProvider formBlockProvider;

	@Before
	public void init() {
		MockitoAnnotations.initMocks( this );
	}

	@Test
	public void composeStepsTest() {

		List<Optional<FormCompositionStep>> formCompositionSteps = getMockFormCompositionSteps( 10 );
		when( formBlockProvider
				.getFormElement( any( Double.class ), any( Lexicon.class ), any( ComposeBlockProvider.class ), any( Form.class ), any( List.class ) ) )
				.thenReturn( formCompositionSteps.get( 0 ) ).thenReturn( formCompositionSteps.get( 1 ) ).thenReturn( formCompositionSteps.get( 2 ) )
				.thenReturn( Optional.empty() ).thenReturn( Optional.empty() ).thenReturn( formCompositionSteps.get( 3 ) ).thenReturn( Optional.empty() )
				.thenReturn( Optional.empty() ).thenReturn( formCompositionSteps.get( 4 ) ).thenReturn( formCompositionSteps.get( 5 ) )
				.thenReturn( formCompositionSteps.get( 6 ) ).thenReturn( formCompositionSteps.get( 7 ) ).thenReturn( formCompositionSteps.get( 8 ) );

		List<FormCompositionStep> compositionSteps = compositionComposer.composeSteps( null, null, "ABCD", JMC.WHOLE_NOTE );
		assertEquals( 4, compositionSteps.size() );
		assertEquals( 4, compositionSteps.get( 0 ).getForm().getValue() );
		assertEquals( 5, compositionSteps.get( 1 ).getForm().getValue() );
		assertEquals( 6, compositionSteps.get( 2 ).getForm().getValue() );
		assertEquals( 7, compositionSteps.get( 3 ).getForm().getValue() );

	}

	@Test
	public void composeStepsTest2() {

		List<Optional<FormCompositionStep>> formCompositionSteps = getMockFormCompositionSteps( 10 );

		when( formBlockProvider
				.getFormElement( any( Double.class ), any( Lexicon.class ), any( ComposeBlockProvider.class ), any( Form.class ), any( List.class ) ) )
				.thenReturn( formCompositionSteps.get( 0 ) ).thenReturn( formCompositionSteps.get( 1 ) ).thenReturn( formCompositionSteps.get( 2 ) )
				.thenReturn( Optional.empty() ).thenReturn( formCompositionSteps.get( 3 ) ).thenReturn( Optional.empty() )
				.thenReturn( formCompositionSteps.get( 4 ) ).thenReturn( formCompositionSteps.get( 5 ) ).thenReturn( formCompositionSteps.get( 6 ) )
				.thenReturn( formCompositionSteps.get( 7 ) ).thenReturn( formCompositionSteps.get( 8 ) );

		List<FormCompositionStep> compositionSteps = compositionComposer.composeSteps( null, null, "ABCD", 2 * JMC.WHOLE_NOTE );
		assertEquals( 4, compositionSteps.size() );
		assertEquals( 0, compositionSteps.get( 0 ).getForm().getValue() );
		assertEquals( 1, compositionSteps.get( 1 ).getForm().getValue() );
		assertEquals( 4, compositionSteps.get( 2 ).getForm().getValue() );
		assertEquals( 5, compositionSteps.get( 3 ).getForm().getValue() );

	}

	@Test
	public void composeStepsFailing() {

		List<Optional<FormCompositionStep>> formCompositionSteps = getMockFormCompositionSteps( 10 );

		when( formBlockProvider
				.getFormElement( any( Double.class ), any( Lexicon.class ), any( ComposeBlockProvider.class ), any( Form.class ), any( List.class ) ) )
				.thenReturn( formCompositionSteps.get( 0 ) ).thenReturn( formCompositionSteps.get( 1 ) ).thenReturn( formCompositionSteps.get( 2 ) )
				.thenReturn( Optional.empty() ).thenReturn( Optional.empty() ).thenReturn( Optional.empty() ).thenReturn( Optional.empty() )
				.thenReturn( formCompositionSteps.get( 3 ) ).thenReturn( Optional.empty() ).thenReturn( Optional.empty() )
				.thenReturn( formCompositionSteps.get( 4 ) ).thenReturn( formCompositionSteps.get( 5 ) ).thenReturn( formCompositionSteps.get( 6 ) )
				.thenReturn( formCompositionSteps.get( 7 ) ).thenReturn( formCompositionSteps.get( 8 ) );

		List<FormCompositionStep> compositionSteps = compositionComposer.composeSteps( null, null, "ABCD", JMC.WHOLE_NOTE );
		assertEquals( 0, compositionSteps.size() );
	}

	private List<Optional<FormCompositionStep>> getMockFormCompositionSteps( int number ) {
		List<Optional<FormCompositionStep>> formCompositionSteps = new ArrayList<>();
		for ( char formCompositionStepNumber = 0; formCompositionStepNumber < number; formCompositionStepNumber++ ) {
			formCompositionSteps.add( Optional.of( getMockFormCompositionStep( formCompositionStepNumber ) ) );
		}
		return formCompositionSteps;
	}

	private FormCompositionStep getMockFormCompositionStep( char id ) {
		FormCompositionStep formCompositionStep = mock( FormCompositionStep.class );
		when( formCompositionStep.getForm() ).thenReturn( new Form( id ) );
		when( formCompositionStep.getCompositionSteps() ).thenReturn( Arrays.asList( new CompositionStep( null, null ) ) );
		return formCompositionStep;
	}
}
