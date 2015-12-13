package persistance.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by Wish on 03.07.2015.
 */
@MappedSuperclass
public abstract class AbstractPersistanceModel {
    @Id
    @GeneratedValue
    Long id;
}
