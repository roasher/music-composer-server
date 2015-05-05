package persistance.dao;

import model.Lexicon;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistance.model.PersistConverter;

import java.io.IOException;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Repository public class LexiconDAO_database implements LexiconDAO {

	@Autowired private SessionFactory sessionFactory;

	@Override public void store( Lexicon lexicon ) throws IOException {
		sessionFactory.getCurrentSession().save( PersistConverter.convertLexicon( lexicon ) );
	}

	@Override public Lexicon fetch() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria( persistance.model.Lexicon.class );
		persistance.model.Lexicon lexicon = ( persistance.model.Lexicon ) criteria.list();
		return PersistConverter.convertLexicon( lexicon );
	}

}
