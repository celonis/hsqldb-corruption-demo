package org.hsqldb.corrupted.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityCorruptedRepository extends CrudRepository<EntityCorrupted, Long> {

}
