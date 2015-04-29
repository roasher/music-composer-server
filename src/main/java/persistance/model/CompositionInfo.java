package persistance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
class CompositionInfo {
	@Id @GeneratedValue
	long id;
	@Column
	String author;
	@Column
	String title;
	@Column
	double tempo;
}
