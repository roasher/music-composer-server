package persistance.dao;

import model.Lexicon;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistance.model.ComposeBlock;
import persistance.PersistConverter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Repository
@Transactional
public class LexiconDAO_database implements LexiconDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PersistConverter persistConverter;

	@Override
	public void store( Lexicon lexicon ) throws IOException {
		persistance.Lexicon persistLexicon = persistConverter.convertLexicon( lexicon );
		for ( ComposeBlock composeBlock : persistLexicon.composeBlockList ) {
			sessionFactory.getCurrentSession().save( composeBlock );
		}
	}

	@Override
	public Lexicon fetch() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria( ComposeBlock.class );
		List<ComposeBlock> persistanceComposeBlockList = criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY ).list();
		List<model.ComposeBlock> composeBlockList = persistConverter.convertPersistComposeBlockList( persistanceComposeBlockList );
		return new Lexicon( composeBlockList );
	}

}
