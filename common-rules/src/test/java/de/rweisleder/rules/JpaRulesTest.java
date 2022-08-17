package de.rweisleder.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.persistence.SequenceGenerator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JpaRulesTest {

    private final ClassFileImporter classFileImporter = new ClassFileImporter();

    @Nested
    class SequenceGeneratorOnFieldTest {

        @Test
        void sequence_generator_on_field_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(SequenceGeneratorOnField.class);
            assertDoesNotThrow(() -> JpaRules.NO_SEQUENCE_GENERATOR_ON_CLASS.check(javaClasses));
        }

        @Test
        void sequence_generator_on_class_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(SequenceGeneratorOnClass.class);
            assertThrows(AssertionError.class, () -> JpaRules.NO_SEQUENCE_GENERATOR_ON_CLASS.check(javaClasses));
        }

        @SuppressWarnings("unused")
        static class SequenceGeneratorOnField {

            @SequenceGenerator(name = "seq")
            private Long id;
        }

        @SequenceGenerator(name = "seq")
        static class SequenceGeneratorOnClass {
        }
    }

    @Nested
    class SequenceGeneratorAllocationSizeTest {

        @Test
        void sequence_generator_with_allocationSize_1_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(AllocationSizeOne.class);
            assertDoesNotThrow(() -> JpaRules.SEQUENCE_GENERATOR_ALLOCATION_SIZE_RULE.check(javaClasses));
        }

        @Test
        void sequence_generator_with_default_allocationSize_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(DefaultAllocationSize.class);
            assertThrows(AssertionError.class, () -> JpaRules.SEQUENCE_GENERATOR_ALLOCATION_SIZE_RULE.check(javaClasses));
        }

        @SuppressWarnings("unused")
        static class AllocationSizeOne {

            @SequenceGenerator(name = "seq", allocationSize = 1)
            private Long id;
        }

        @SuppressWarnings("unused")
        static class DefaultAllocationSize {

            @SequenceGenerator(name = "seq")
            private Long id;
        }
    }
}