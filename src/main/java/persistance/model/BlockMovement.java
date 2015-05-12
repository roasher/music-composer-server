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
	int topVoiceMovement;
	@Column
	int bottomVoiceMovement;

	BlockMovement() {}
	BlockMovement( int topVoiceMovement, int bottomVoiceMovement ) {
		this.topVoiceMovement = topVoiceMovement;
		this.bottomVoiceMovement = bottomVoiceMovement;
	}

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof BlockMovement ) )
			return false;

		BlockMovement that = ( BlockMovement ) o;

		if ( topVoiceMovement != that.topVoiceMovement )
			return false;
		return bottomVoiceMovement == that.bottomVoiceMovement;

	}

	@Override public int hashCode() {
		int result = topVoiceMovement;
		result = 31 * result + bottomVoiceMovement;
		return result;
	}
}
