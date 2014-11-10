package decomposer;

import decomposer.analyzer.form.FormAnalyzer;
import jm.music.data.Note;
import jm.music.data.Score;
import model.MusicBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides form analyses
 * Created by night wish on 08.11.14.
 */
public class FormDecomposer {

	private FormAnalyzer formAnalyzer;

//	public List< Integer > decompose( Score score ) {
//		List< Integer > form = new ArrayList<>();
//		List<MusicBlock> musicBlocks = getBlocks( score );
//		for ( MusicBlock musicBlock : musicBlocks ) {
//			form.add( musicBlocks.analyzeBlock( musicBlock ) );
//		}
//		return form;
//	}
//
//	public List< MusicBlock > getBlocks( Score score ) {
//		List< List<Note> > listOfInstrumentPart = new ArrayList<>();
//		List< Note > instrumentPart = new ArrayList<>();
//		MusicBlock musicBlock = new MusicBlock( );
//	}
//
//	public FormAnalyzer getFormAnalyzer() {
//		return formAnalyzer;
//	}
//
//	public void setFormAnalyzer( FormAnalyzer formAnalyzer ) {
//		this.formAnalyzer = formAnalyzer;
//	}
}
