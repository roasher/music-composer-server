package persistance.jpa.factory;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public abstract class AbstractFactory<T> {

	protected Set<T> uniqueValueSet = new HashSet<>();

	protected T getUniqueInstance( T object ) {
		for ( T currentObject : this.uniqueValueSet ) {
			if ( currentObject.equals( object ) ) {
				return currentObject;
			}
		}
		this.uniqueValueSet.add( object );
		return object;
	}

	public void reset() {
		uniqueValueSet = new HashSet<>(  );
	}

}
