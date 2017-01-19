package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.ComposeBlock;

@Repository
public interface ComposeBlockRepository extends CrudRepository<ComposeBlock, Long> {
}
