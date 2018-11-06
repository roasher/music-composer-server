package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LexiconDB implements Serializable {

	private List<MusicBlock> musicBlocks;
	private Map<Integer, List<Integer>> possibleNextMusicBlockNumbers;

}
