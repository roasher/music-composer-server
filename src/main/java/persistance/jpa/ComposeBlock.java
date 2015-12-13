package persistance.jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity( name = "COMPOSE_BLOCK" )
public class ComposeBlock extends AbstractPersistanceModel {

	@Column( name = "START_TIME" )
	public double startTime;
	@ManyToOne( cascade = CascadeType.ALL )
	@JoinColumn(name="COMPOSITION_INFO_ID")
	public CompositionInfo compositionInfo;
	@ManyToMany( cascade = CascadeType.ALL )
	@JoinTable( name = "BLOCK_MELODY", joinColumns = {@JoinColumn( name = "BLOCK_ID" )}, inverseJoinColumns = {@JoinColumn( name = "MELODY_ID")} )
	public List<Melody> melodies;
	@ManyToOne( cascade = CascadeType.ALL )
	@JoinColumn(name="BLOCK_MOVEMENT_ID")
	public BlockMovement blockMovementFromPreviousToThis;

	@ManyToMany( mappedBy = "possiblePreviousComposeBlocks" )
	public List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );

	@ManyToMany
	@JoinTable( name = "BLOCK_RELATION", joinColumns = {@JoinColumn( name = "BLOCK_ID" )})
	public List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	ComposeBlock() {}
	public ComposeBlock(double startTime, CompositionInfo compositionInfo, List<Melody> melodies, BlockMovement blockMovementFromPreviousToThis) {
		this.startTime = startTime;
		this.compositionInfo = compositionInfo;
		this.melodies = melodies;
		this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
	}

	ComposeBlock( double startTime, CompositionInfo compositionInfo, List<Melody> melodies, BlockMovement blockMovementFromPreviousToThis,
			List<ComposeBlock> possibleNextComposeBlocks, List<ComposeBlock> possiblePreviousComposeBlocks ) {
		this( startTime, compositionInfo, melodies, blockMovementFromPreviousToThis );
		this.possibleNextComposeBlocks = possibleNextComposeBlocks;
		this.possiblePreviousComposeBlocks = possiblePreviousComposeBlocks;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( Melody melody : this.melodies ) {
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
		if ( !melodies.equals( that.melodies ) )
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
		result = 31 * result + melodies.hashCode();
		result = 31 * result + ( blockMovementFromPreviousToThis != null ?
				blockMovementFromPreviousToThis.hashCode() :
				0 );
		result = 31 * result + possibleNextComposeBlocks.size();
		result = 31 * result + possiblePreviousComposeBlocks.size();
		return result;
	}

	public boolean isSimilar( ComposeBlock composeBlock ) {
		boolean isEqualStartTimes = Double.compare( this.startTime, composeBlock.startTime ) == 0;

		boolean isEqualCompositionInfos;
		if ( this.compositionInfo != null ) {
			isEqualCompositionInfos = this.compositionInfo.equals( composeBlock.compositionInfo );
		} else {
			isEqualCompositionInfos = composeBlock.compositionInfo == null;
		}

		boolean isEqualMelodyList = this.melodies.equals( composeBlock.melodies );

		boolean isEqualsBlockMovements;
		if ( this.blockMovementFromPreviousToThis != null ) {
			isEqualsBlockMovements = this.blockMovementFromPreviousToThis.equals( composeBlock.blockMovementFromPreviousToThis );
		} else {
			isEqualsBlockMovements = composeBlock.blockMovementFromPreviousToThis == null;
		}

		return isEqualStartTimes && isEqualCompositionInfos && isEqualMelodyList && isEqualsBlockMovements;
	}
}
