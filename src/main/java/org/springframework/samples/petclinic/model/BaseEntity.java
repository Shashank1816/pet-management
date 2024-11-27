
package org.springframework.samples.petclinic.model;

import java.io.Serializable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(BaseEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null) {
            logger.warn("Attempted to set a null ID on entity: {}", this);
        }
        this.id = id;
    }

    public boolean isNew() {
        boolean isNew = (this.id == null);
        logger.debug("Entity {} is new: {}", this, isNew);
        return isNew;
    }

    @Override
    public String toString() {
        return String.format("BaseEntity{id=%d}", id);
    }
}

