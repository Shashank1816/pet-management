
package org.springframework.samples.petclinic;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

public class PetClinicRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        registerResourcePatterns(hints.resources());
        registerSerializationTypes(hints.serialization());
    }

    private void registerResourcePatterns(RuntimeHints.Resources resources) {
        resources.registerPattern("db/*");
        resources.registerPattern("messages/*");
        resources.registerPattern("META-INF/resources/webjars/*");
        resources.registerPattern("mysql-default-conf");
    }

    private void registerSerializationTypes(RuntimeHints.Serialization serialization) {
        serialization.registerType(BaseEntity.class);
        serialization.registerType(Person.class);
        serialization.registerType(Vet.class);
    }

}

