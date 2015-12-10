package persistance.prototype;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by night wish on 01.06.2015.
 */
@Entity
public class Vagon {
	@Id @Column( name = "ID" )
	public int id;
	@Column( name = "NAME" )
	public String name;

	public Vagon() {}
	public Vagon( int id, String name ) { this.id = id; this.name = name; }

	@ManyToMany
	@JoinTable( name = "VAGON_RELATION", joinColumns = {@JoinColumn( name = "VAGON_ID" )})
	public Set<Vagon> previousVagons = new HashSet<>(  );

	@ManyToMany( mappedBy = "previousVagons" )
	public Set<Vagon> nextVagons = new HashSet<>(  );

}
