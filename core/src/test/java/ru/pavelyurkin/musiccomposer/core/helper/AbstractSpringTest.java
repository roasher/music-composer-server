package ru.pavelyurkin.musiccomposer.core.helper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.pavelyurkin.musiccomposer.core.MyTestConfiguration;

/**
 * Parent test class
 * Created by pyurkin on 05.12.14.
 */
@SpringBootTest
@ContextConfiguration(classes = MyTestConfiguration.class)
public abstract class AbstractSpringTest {

}