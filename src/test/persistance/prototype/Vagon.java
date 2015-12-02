package persistance.prototype;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by night wish on 01.06.2015.
 */
@Entity
public class Vagon {
	@Id @Column( name = "VAGON_ID" )
	public int id;
	@Column( name = "NAME" )
	public String name;

	public Vagon() {}
	public Vagon( int id, String name ) { this.id = id; this.name = name; }

	@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinTable( name = "VAGON_RELATION", joinColumns = {@JoinColumn( name = "LEFT_VAGON_ID" )})
	public Set<Vagon> possiblePreviousVagons = new HashSet<>(  );

	@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "possiblePreviousVagons" )
	public Set<Vagon> possibleNextVagons = new HashSet<>(  );

}
