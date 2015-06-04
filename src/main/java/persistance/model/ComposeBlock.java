package persistance.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
public class ComposeBlock {
	@Id @GeneratedValue @Column( name = "COMPOSE_BLOCK_ID" )
	public long id;
	@Column
	public double startTime;
	@ManyToOne( cascade = CascadeType.ALL )
	public CompositionInfo compositionInfo;
	@OneToMany( cascade = CascadeType.ALL )
	public List<Melody> melodyList;
	@ManyToOne( cascade = CascadeType.ALL )
	public BlockMovement blockMovementFromPreviousToThis;

	@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "possiblePreviousComposeBlocks" )
	public List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );

	@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinTable( name = "COMPOSE_BLOCKS_NEXT_REL",
			joinColumns = {@JoinColumn( name = "COMPOSE_BLOCK_ID" )},
			inverseJoinColumns = {@JoinColumn( name = "PREV_COMPOSE_BLOCK_ID" )})
	public List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	ComposeBlock() {}
	ComposeBlock( double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, BlockMovement blockMovementFromPreviousToThis ) {
		this.startTime = startTime;
		this.compositionInfo = compositionInfo;
		this.melodyList = melodyList;
		this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
	}

	ComposeBlock( double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, BlockMovement blockMovementFromPreviousToThis,
			List<ComposeBlock> possibleNextComposeBlocks, List<ComposeBlock> possiblePreviousComposeBlocks ) {
		this( startTime, compositionInfo, melodyList, blockMovementFromPreviousToThis );
		this.possibleNextComposeBlocks = possibleNextComposeBlocks;
		this.possiblePreviousComposeBlocks = possiblePreviousComposeBlocks;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( Melody melody : this.melodyList ) {
			stringBuilder.append('|').append( melody.toString() );
		}
		return stringBuilder.toString();
	}

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof ComposeBlock ) )
			return false;

		ComposeBlock that = ( ComposeBlock ) o;

		if ( Double.compare( that.startTime, startTime ) != 0 )
			return false;
		if ( compositionInfo != null ? !compositionInfo.equals( that.compositionInfo ) : that.compositionInfo != null )
			return false;
		if ( !melodyList.equals( that.melodyList ) )
			return false;

		if ( !isEquals( this.possibleNextComposeBlocks, that.possibleNextComposeBlocks ) ) return false;
		if ( !isEquals( this.possiblePreviousComposeBlocks, that.possiblePreviousComposeBlocks ) ) return false;

		return true;
	}

	/**
	 * Two lists considered equals if they have equal size and every entry from one has similar entry from another
	 * @param firstComposeBlockList
	 * @param secondComposeBlockList
	 * @return
	 */
	private boolean isEquals( List<ComposeBlock> firstComposeBlockList, List<ComposeBlock> secondComposeBlockList ) {
		if ( firstComposeBlockList.size() != secondComposeBlockList.size() ) return false;

		for( ComposeBlock firstComposeBlock : firstComposeBlockList ) {
			boolean isInList = false;
			for ( ComposeBlock secondComposeBlock : secondComposeBlockList ) {
				if ( ( firstComposeBlock == null && secondComposeBlock != null ) ||
						( firstComposeBlock != null && secondComposeBlock == null ) ) {
					continue;
				} else if ( firstComposeBlock == null && secondComposeBlock == null ||
						firstComposeBlock.isSimilar( secondComposeBlock ) ) {
					isInList = true;
					break;
				}
			}
			if ( !isInList ) return false;
		}
		return true;
	}

	@Override public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits( startTime );
		result = ( int ) ( temp ^ ( temp >>> 32 ) );
		result = 31 * result + ( compositionInfo != null ? compositionInfo.hashCode() : 0 );
		result = 31 * result + melodyList.hashCode();
		result = 31 * result + ( blockMovementFromPreviousToThis != null ?
				blockMovementFromPreviousToThis.hashCode() :
				0 );
		result = 31 * result + possibleNextComposeBlocks.size();
		result = 31 * result + possiblePreviousComposeBlocks.size();
		return result;
	}

	public boolean isSimilar( persistance.model.ComposeBlock composeBlock ) {
		boolean isEqualStartTimes = Double.compare( this.startTime, composeBlock.startTime ) == 0;

		boolean isEqualCompositionInfos;
		if ( this.compositionInfo != null ) {
			isEqualCompositionInfos = this.compositionInfo.equals( composeBlock.compositionInfo );
		} else {
			isEqualCompositionInfos = composeBlock.compositionInfo == null;
		}

		boolean isEqualMelodyList = this.melodyList.equals( composeBlock.melodyList );

		boolean isEqualsBlockMovements;
		if ( this.blockMovementFromPreviousToThis != null ) {
			isEqualsBlockMovements = this.blockMovementFromPreviousToThis.equals( composeBlock.blockMovementFromPreviousToThis );
		} else {
			isEqualsBlockMovements = composeBlock.blockMovementFromPreviousToThis == null;
		}

		return isEqualStartTimes && isEqualCompositionInfos && isEqualMelodyList && isEqualsBlockMovements;
	}
}
