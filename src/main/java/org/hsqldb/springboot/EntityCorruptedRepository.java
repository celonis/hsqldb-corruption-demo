package org.hsqldb.springboot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityCorruptedRepository extends CrudRepository<EntityCorrupted, Long> {

}
