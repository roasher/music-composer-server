package persistance.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
class Melody {
	@Id @GeneratedValue
	long id;
	@ManyToMany
	List<Note> noteList;
}
