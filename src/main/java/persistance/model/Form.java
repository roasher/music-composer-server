package persistance.model;


import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by night wish on 06.06.2015.
 */
@Entity
public class Form extends AbstractPersistanceModel {

	@Column
	public
	char value;

	public Form() {}

	public Form( char value ) {
		this.value = value;
	}
}
