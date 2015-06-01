package persistance.test;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by night wish on 01.06.2015.
 */
@Entity
public class Vagon {
	@Id @GeneratedValue @Column( name = "VAGON_ID" )
	public int id;
	@Column( name = "NAME" )
	public String name;

	public Vagon() {}
	public Vagon( String name ) { this.name = name; }

	@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinTable( name = "VAGON_NEXT_REL",
		joinColumns = {@JoinColumn( name = "VAGON_ID" )},
		inverseJoinColumns = {@JoinColumn( name = "PREV_VAGON_ID" )})
	public Set<Vagon> possiblePreviousVagons = new HashSet<>(  );

	@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "possiblePreviousVagons" )
//	@JoinTable( name = "VAGON_PREV_REL",
//			joinColumns = {@JoinColumn( name = "VAGON_ID" )},
//			inverseJoinColumns = {@JoinColumn( name = "PREV_VAGON_ID" )})
	public Set<Vagon> possibleNextVagons = new HashSet<>(  );
}
