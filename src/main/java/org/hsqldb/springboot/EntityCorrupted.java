package org.hsqldb.springboot;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Cacheable
public class EntityCorrupted {

    @Id
    @GeneratedValue
    private Long id;


    @Lob
    private String corruptedValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorruptedValue() {
        return corruptedValue;
    }

    public void setCorruptedValue(String corruptedValue) {
        this.corruptedValue = corruptedValue;
    }
}
