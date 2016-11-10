package ru.pavelyurkin.musiccomposer.persistance.dao;

import ru.pavelyurkin.musiccomposer.model.Lexicon;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.pavelyurkin.musiccomposer.persistance.jpa.ComposeBlock;
import ru.pavelyurkin.musiccomposer.persistance.PersistConverter;

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
	public void persist( Lexicon lexicon ) throws IOException {
		List <ru.pavelyurkin.musiccomposer.persistance.jpa.ComposeBlock> composeBlocks = persistConverter.convertComposeBlockList( lexicon );
		for ( ComposeBlock composeBlock : composeBlocks ) {
			sessionFactory.getCurrentSession().save( composeBlock );
		}
	}

	@Override
	public Lexicon fetch() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria( ComposeBlock.class );
		List<ComposeBlock> persistanceComposeBlockList = criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY ).list();
		Lexicon lexicon = persistConverter.convertPersistComposeBlockList( persistanceComposeBlockList );
		return lexicon;
	}

}
