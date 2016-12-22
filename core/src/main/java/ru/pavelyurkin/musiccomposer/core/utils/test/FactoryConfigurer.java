package ru.pavelyurkin.musiccomposer.core.utils.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.AbstractFactory;

import java.util.Map;

/**
 * Created by wish on 15.12.2015.
 */
@Component
public class FactoryConfigurer implements ApplicationContextAware {

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void resetAllFactories() {
		Map<String, AbstractFactory> abstractFactoryMap = applicationContext.getBeansOfType( AbstractFactory.class );
		for ( AbstractFactory abstractFactory : abstractFactoryMap.values() ) {
			abstractFactory.reset();
		}
	}
}
