package persistance.model;


import javax.persistence.*;
import java.util.ArrayList;
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
	List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );
	@ManyToMany
	List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	ComposeBlock() {}
	ComposeBlock( MusicBlock musicBlock ) { this.musicBlock = musicBlock; }
	ComposeBlock( MusicBlock musicBlock, List<ComposeBlock> possibleNextComposeBlocks, List<ComposeBlock> possiblePreviousComposeBlocks ) {
		this( musicBlock );
		this.possibleNextComposeBlocks = possibleNextComposeBlocks;
		this.possiblePreviousComposeBlocks = possiblePreviousComposeBlocks;
	}

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof ComposeBlock ) )
			return false;

		ComposeBlock that = ( ComposeBlock ) o;

		if ( !musicBlock.equals( that.musicBlock ) )
			return false;
		if ( !possibleNextComposeBlocks.equals( that.possibleNextComposeBlocks ) )
			return false;
		if ( !possiblePreviousComposeBlocks.equals( that.possiblePreviousComposeBlocks ) )
			return false;

		return true;
	}

	@Override public int hashCode() {
		int result = musicBlock.hashCode();
		result = 31 * result + possibleNextComposeBlocks.size();
		result = 31 * result + possiblePreviousComposeBlocks.size();
		return result;
	}

	@Override
	public String toString() {
		return this.musicBlock.toString();
	}
}
