package ru.pavelyurkin.musiccomposer.core.service.composition.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.util.Read;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;

/**
 * Class is responsible for loading midi tracks
 */
@Component
public class CompositionLoader {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RhythmValueHandler rhythmValueHandler;

  private List<File> listFilesForFolder(final File folder) {
    List<File> nameArray = new ArrayList<>();
    File[] files = folder.listFiles();
    if (files == null || files.length == 0) {
      return Collections.emptyList();
    }
    for (final File fileEntry : files) {
      if (!fileEntry.isDirectory()) {
        nameArray.add(fileEntry);
      }
    }
    return nameArray;
  }

  /**
   * Retrieve all midi composition from folder except those in exclude pattern list
   *
   * @param folder
   * @param excludePatternList
   * @return
   */
  public List<Composition> getCompositionsFromFolder(File folder, List<String> excludePatternList) {
    List<Composition> compositions = new ArrayList<Composition>();
    List<File> listFiles = listFilesForFolder(folder);
    for (File currentFile : listFiles) {
      if (!currentFile.getName().matches(".*\\.mid")) {
        continue;
      }
      for (String excludePattern : excludePatternList) {
        if (currentFile.getName().matches(excludePattern)) {
          continue;
        }
        //".*drum.*"
      }
      Composition composition = getComposition(currentFile);
      compositions.add(composition);
    }
    if (compositions.isEmpty()) {
      throw new RuntimeException("No compositions in this folder: " + folder.getAbsolutePath());
    }
    return compositions;
  }

  /**
   * Retrieve all midi compositions from folders
   *
   * @param folder
   * @return
   */
  public List<Composition> getCompositionsFromFolder(File folder) {
    return getCompositionsFromFolder(folder, Collections.<String>emptyList());
  }

  public List<Composition> getCompositions(File... files) {
    List<Composition> compositions = new ArrayList<>();
    for (File currentDirectory : files) {
      compositions.add(getComposition(currentDirectory));
    }
    return compositions;
  }

  public Composition getComposition(File file) {
    logger.info("Reading composition {}", file);
    CompositionInfo compositionInfo = new CompositionInfo();
    compositionInfo.setTitle(file.getName());

    Composition composition = new Composition();
    composition.setCompositionInfo(compositionInfo);
    composition.setTitle(file.getName());

    Read.midi(composition, file.getAbsolutePath());
    roundAllRhythmValues(composition);
    return composition;
  }

  private void roundAllRhythmValues(Composition composition) {
    for (Part part : composition.getPartArray()) {
      for (Phrase phrase : part.getPhraseArray()) {
        for (Note note : (List<Note>) phrase.getNoteList()) {
          double newValue = rhythmValueHandler.roundRhythmValue(note.getRhythmValue());
          note.setRhythmValue(newValue);
        }
      }
    }
  }

}
