
package org.springframework.samples.petclinic;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

public class PetClinicRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // Register resource patterns
        registerResourcePatterns(hints.resources());
        
        // Register serialization types
        registerSerializationTypes(hints.serialization());
    }

    private void registerResourcePatterns(RuntimeHints.Resources resources) {
        // Register database resources pattern
        registerDatabaseResources(resources);
        
        // Register message resources pattern
        registerMessageResources(resources);
        
        // Register webjars resources pattern
        registerWebJarsResources(resources);
        
        // Register default configuration resource pattern
        registerDefaultConfResource(resources);
    }

    private void registerDatabaseResources(RuntimeHints.Resources resources) {
        // Register pattern for database resources
        resources.registerPattern("db/*");
    }

    private void registerMessageResources(RuntimeHints.Resources resources) {
        // Register pattern for message resources
        resources.registerPattern("messages/*");
    }

    private void registerWebJarsResources(RuntimeHints.Resources resources) {
        // Register pattern for webjars resources
        resources.registerPattern("META-INF/resources/webjars/*");
    }

    private void registerDefaultConfResource(RuntimeHints.Resources resources) {
        // Register pattern for default configuration resource
        resources.registerPattern("mysql-default-conf");
    }

    private void registerSerializationTypes(RuntimeHints.Serialization serialization) {
        // Register serialization types
        serialization.registerType(BaseEntity.class);
        serialization.registerType(Person.class);
        serialization.registerType(Vet.class);
    }

}

