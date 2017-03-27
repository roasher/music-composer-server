package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.persistance.PersistConverter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Component
public class LexiconDAO_database implements LexiconDAO {

	@Autowired
	private ComposeBlockRepository composeBlockRepository;

	@Autowired
	private PersistConverter persistConverter;

	@Override
	@Transactional
	public void persist( Lexicon lexicon ) throws IOException {
		List <ComposeBlock> composeBlocks = persistConverter.convertComposeBlockList( lexicon );
		for ( ComposeBlock composeBlock : composeBlocks ) {
			composeBlockRepository.save( composeBlock );
		}
	}

	@Override
	@Transactional
	public Lexicon fetch() {
		List<ComposeBlock> composeBlocks = ( List<ComposeBlock> ) composeBlockRepository.findAll();
		Lexicon lexicon = persistConverter.convertPersistComposeBlockList( composeBlocks );
		return lexicon;
	}

}