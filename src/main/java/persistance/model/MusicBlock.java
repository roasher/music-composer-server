package persistance.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
class MusicBlock {
	@Id @GeneratedValue
	long id;
	@Column
	ComposeBlock composeBlock;
	@ManyToMany
	List<Melody> melodyList;
	@ManyToMany
	MusicBlock next;
	@ManyToMany
	MusicBlock previous;
}
