package decomposer.analyzer.form;

import model.MusicBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by pyurkin on 12.11.14.
 */
@Component
public class FormAnalyzer {

	@Autowired
	private FormEqualityAnalyzerImpl formEqualityAnalyzer;

	public boolean equal( MusicBlock firstMusicBlock, MusicBlock secondMusicBlock ) {
//		formEqualityAnalyzer.isEqual(  )
		return false;
	}
}
