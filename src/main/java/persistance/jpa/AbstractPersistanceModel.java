package persistance.jpa;

import javax.persistence.*;

/**
 * Created by Wish on 03.07.2015.
 */
@MappedSuperclass
public abstract class AbstractPersistanceModel {
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ" )
	Long id;
}
