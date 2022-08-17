package de.rweisleder.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersistenceRulesTest {

    private final ClassFileImporter classFileImporter = new ClassFileImporter();

    @Nested
    class TransactionalEntityManagerTest {

        @Test
        void transactional_method_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(TransactionalMethod.class);
            assertDoesNotThrow(() -> PersistenceRules.TRANSACTIONAL_ACCESS_TO_ENTITY_MANAGER.check(javaClasses));
        }

        @Test
        void transactional_class_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(TransactionalClass.class);
            assertDoesNotThrow(() -> PersistenceRules.TRANSACTIONAL_ACCESS_TO_ENTITY_MANAGER.check(javaClasses));
        }

        @Test
        void non_transactional_method_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(NonTransactionalMethod.class);
            assertThrows(AssertionError.class, () -> ServiceRules.SERVICES_SHOULD_BE_SECURED.check(javaClasses));
        }

        @SuppressWarnings("unused")
        static class TransactionalMethod {
            private EntityManager em;

            @Transactional
            public void doSomething() {
                em.flush();
            }
        }

        @Transactional
        @SuppressWarnings("unused")
        static class TransactionalClass {
            private EntityManager em;

            public void doSomething() {
                em.flush();
            }
        }

        @SuppressWarnings("unused")
        static class NonTransactionalMethod {
            private EntityManager em;

            public void doSomething() {
                em.flush();
            }
        }
    }
}