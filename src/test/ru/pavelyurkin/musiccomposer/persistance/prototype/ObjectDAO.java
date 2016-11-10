package ru.pavelyurkin.musiccomposer.persistance.prototype;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by night wish on 01.06.2015.
 */
@Repository
@Transactional
public class ObjectDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void persist( Object object ) {
		sessionFactory.getCurrentSession().save( object );
	}

	public void persist( List objectList ) {
		Session session = sessionFactory.getCurrentSession();
		for( Object entry : objectList ) {
			session.save( entry );
		}
	}

	public List<Object> getAll() {
		return sessionFactory.getCurrentSession().createCriteria( Vagon.class ).setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY ).list();
	}

	public List<Object> get() {
		return null;
	}
}
