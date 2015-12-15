package utils.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by wish on 15.12.2015.
 */
public class DbConfigurer implements ApplicationContextAware {

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void resetAllSequences() {
		// TODO implement
	}

}
