package de.rweisleder.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceRulesTest {

    private final ClassFileImporter classFileImporter = new ClassFileImporter();

    @Nested
    class ServiceClassNameSuffixTest {

        @Test
        void service_with_correct_suffix_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(UserService.class);
            assertDoesNotThrow(() -> ServiceRules.SERVICE_CLASS_NAMES_SHOULD_HAVE_SUFFIX.check(javaClasses));
        }

        @Test
        void service_with_wrong_suffix_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(UserSevice.class);
            assertThrows(AssertionError.class, () -> ServiceRules.SERVICE_CLASS_NAMES_SHOULD_HAVE_SUFFIX.check(javaClasses));
        }

        @Service
        static class UserService {
        }

        @Service
        static class UserSevice {
        }
    }

    @Nested
    class ServiceMethodLoggingTest {

        @Test
        void service_method_without_logging_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(ServiceWithoutLogging.class);
            assertThrows(AssertionError.class, () -> ServiceRules.SERVICE_METHODS_SHOULD_LOG.check(javaClasses));
        }

        @Test
        void service_method_with_logging_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(ServiceWithLogging.class);
            assertDoesNotThrow(() -> ServiceRules.SERVICE_METHODS_SHOULD_LOG.check(javaClasses));
        }

        @Service
        @SuppressWarnings("unused")
        static class ServiceWithoutLogging {

            private static final Logger log = LoggerFactory.getLogger(ServiceWithLogging.class);

            public void doSomething() {
            }
        }

        @Service
        @SuppressWarnings("unused")
        static class ServiceWithLogging {

            private static final Logger log = LoggerFactory.getLogger(ServiceWithLogging.class);

            public void doSomething() {
                log.info("Doing something");
            }
        }
    }

    @Nested
    class SecuredServiceMethodsTest {

        @Test
        void service_with_all_methods_secured_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(AllMethodsSecured.class);
            assertDoesNotThrow(() -> ServiceRules.SERVICES_SHOULD_BE_SECURED.check(javaClasses));
        }

        @Test
        void service_with_unsecured_method_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(OneMethodNotSecured.class);
            assertThrows(AssertionError.class, () -> ServiceRules.SERVICES_SHOULD_BE_SECURED.check(javaClasses));
        }

        @Service
        @SuppressWarnings("unused")
        static class AllMethodsSecured {

            @Secured("x")
            public void doSomething() {
            }

            @Secured("x")
            public void doSomethingElse() {
            }
        }

        @Service
        @SuppressWarnings("unused")
        static class OneMethodNotSecured {

            @Secured("x")
            public void doSomething() {
            }

            public void doSomethingElse() {
            }
        }
    }
}