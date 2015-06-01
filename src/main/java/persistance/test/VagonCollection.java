package persistance.test;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by night wish on 01.06.2015.
 */
@Entity
public class VagonCollection {
	@Id @GeneratedValue
	public int id;
	@Column
	public String name;
	@OneToMany
	public List<Vagon> vagonList;

	public VagonCollection( String name, List<Vagon> vagonList ) {
		this.name = name;
		this.vagonList = vagonList;
	}
}
