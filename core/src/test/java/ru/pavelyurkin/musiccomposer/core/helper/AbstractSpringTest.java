package ru.pavelyurkin.musiccomposer.core.helper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.pavelyurkin.musiccomposer.core.MyTestConfiguration;

/**
 * Parent test class
 */
@SpringBootTest
@ContextConfiguration(classes = MyTestConfiguration.class)
public abstract class AbstractSpringTest {

}