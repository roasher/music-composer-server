package persistance.model;


import javax.persistence.*;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
class ComposeBlock {
	@Id @GeneratedValue
	long id;
	@Column
	MusicBlock musicBlock;
	@ManyToMany
	List<ComposeBlock> possibleNextComposeBlocks;
	@ManyToMany
	List<ComposeBlock> possiblePreviousComposeBlocks;
}
