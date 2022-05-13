package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LexiconDB implements Serializable {

  private List<MusicBlock> musicBlocks;
  private Map<Integer, Set<Integer>> possibleNextMusicBlockNumbers;

}
