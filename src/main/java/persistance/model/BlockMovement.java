package persistance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by pyurkin on 06.05.2015.
 */
@Entity
public class BlockMovement {
	@Id @GeneratedValue
	long id;
	@Column
	MelodyMovement topVoiceMovement;
	@Column
	MelodyMovement bottomVoiceMovement;
}
