package persistance.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
public class Lexicon {
	@Id @GeneratedValue
	long id;
	@Column
	double minRhythmValue;
	@ManyToMany
	List<ComposeBlock> composeBlockList;
}
