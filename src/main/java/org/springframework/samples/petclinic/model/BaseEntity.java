
package org.springframework.samples.petclinic.model;

import java.io.Serializable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseEntity serves as a base class for all entities in the application.
 * It provides common properties and methods that can be shared across different entities.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; // Serialization ID for the class

    // Logger instance for logging messages related to this class
    private static final Logger logger = LoggerFactory.getLogger(BaseEntity.class);

    // Unique identifier for the entity, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Gets the ID of the entity.
     * 
     * @return the ID of the entity
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the entity.
     * If the provided ID is null, a warning is logged.
     * 
     * @param id the ID to set
     */
    public void setId(Integer id) {
        if (id == null) {
            // Log a warning if an attempt is made to set a null ID
            logger.warn("Attempted to set a null ID on entity: {}", this);
        }
        this.id = id; // Set the ID regardless of null check
    }

    /**
     * Checks if the entity is new (i.e., has not been persisted yet).
     * Logs the status of the entity.
     * 
     * @return true if the entity is new, false otherwise
     */
    public boolean isNew() {
        boolean isNew = this.id == null; // Determine if the entity is new
        // Log the new status of the entity
        logger.debug("Entity {} is new: {}", this, isNew);
        return isNew; // Return the new status
    }

    /**
     * Provides a string representation of the BaseEntity.
     * This is useful for logging and debugging purposes.
     * 
     * @return a string representation of the entity
     */
    @Override
    public String toString() {
        return "BaseEntity{id=" + id + "}"; // Return the ID in the string representation
    }
}

