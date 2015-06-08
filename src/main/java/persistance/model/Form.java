package persistance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by night wish on 06.06.2015.
 */
@Entity
class Form {
	@Id @GeneratedValue
	long id;
	@Column
	char value;

	public Form() {}

	public Form( char value ) {
		this.value = value;
	}
}
