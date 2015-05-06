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
	double rhythmValue;
	@Column
	double startTime;
	@Column
	CompositionInfo compositionInfo;
	@OneToMany
	List<Melody> melodyList;
	@ManyToOne
	List<Integer> startIntervalPattern;
	@ManyToOne
	BlockMovement blockMovementFromPreviousToThis;

	@ManyToMany
	List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );
	@ManyToMany
	List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	ComposeBlock() {}
	ComposeBlock( double rhythmValue, double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, List<Integer> startIntervalPattern, BlockMovement blockMovementFromPreviousToThis ) {
		this.rhythmValue = rhythmValue;
		this.startTime = startTime;
		this.compositionInfo = compositionInfo;
		this.melodyList = melodyList;
		this.startIntervalPattern = startIntervalPattern;
		this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
	}

	ComposeBlock( double rhythmValue, double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, List<Integer> startIntervalPattern, BlockMovement blockMovementFromPreviousToThis,
			List<ComposeBlock> possibleNextComposeBlocks, List<ComposeBlock> possiblePreviousComposeBlocks ) {
		this( rhythmValue, startTime, compositionInfo, melodyList, startIntervalPattern, blockMovementFromPreviousToThis );
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
}
