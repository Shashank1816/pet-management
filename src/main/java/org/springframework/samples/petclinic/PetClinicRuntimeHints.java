package org.springframework.samples.petclinic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

public class PetClinicRuntimeHints implements RuntimeHintsRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(PetClinicRuntimeHints.class);

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        try {
            registerResourcePatterns(hints.resources());
            registerSerializationTypes(hints.serialization());
        } catch (Exception e) {
            logger.error("Error registering runtime hints", e);
        }
    }

    private void registerResourcePatterns(RuntimeHints.Resources resources) {
        try {
            registerDatabaseResources(resources);
            registerMessageResources(resources);
            registerWebJarsResources(resources);
            registerDefaultConfResource(resources);
        } catch (Exception e) {
            logger.error("Error registering resource patterns", e);
        }
    }

    private void registerDatabaseResources(RuntimeHints.Resources resources) {
        resources.registerPattern("db/*");
    }

    private void registerMessageResources(RuntimeHints.Resources resources) {
        resources.registerPattern("messages/*");
    }

    private void registerWebJarsResources(RuntimeHints.Resources resources) {
        resources.registerPattern("META-INF/resources/webjars/*");
    }

    private void registerDefaultConfResource(RuntimeHints.Resources resources) {
        resources.registerPattern("mysql-default-conf");
    }

    private void registerSerializationTypes(RuntimeHints.Serialization serialization) {
        try {
            serialization.registerType(BaseEntity.class);
            serialization.registerType(Person.class);
            serialization.registerType(Vet.class);
        } catch (Exception e) {
            logger.error("Error registering serialization types", e);
        }
    }
}
