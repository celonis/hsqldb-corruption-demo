package org.hsqldb.corrupted.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Cacheable
public class ClobCorrupted {

    @Id
    @GeneratedValue
    private Long id;

    private Integer expectedLength;

    @Lob
    @Column(length = 1024 * 1024 * 1024) // 1GiB
    private String corruptedValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExpectedLength() {
        return expectedLength;
    }

    public void setExpectedLength(Integer expectedLength) {
        this.expectedLength = expectedLength;
    }

    public String getCorruptedValue() {
        return corruptedValue;
    }

    public void setCorruptedValue(String corruptedValue) {
        this.corruptedValue = corruptedValue;
    }
}
