package ru.pavelyurkin.musiccomposer.core.helper;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.pavelyurkin.musiccomposer.core.Application;

/**
 * Created by Wish on 21.12.2015.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = Application.class )
public abstract class AbstractSpringComposerTest {
}
