package de.rweisleder.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import de.rweisleder.rules.CodingRulesTest.AccessStandardStreamsTest.ClassWithSystemOut;
import de.rweisleder.rules.CodingRulesTest.NoLog4JTest.ClassWithLog4J;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CodingRulesTest {

    private final ClassFileImporter classFileImporter = new ClassFileImporter();

    @Nested
    class AccessStandardStreamsTest {

        @Test
        void e_printStackTrace_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(ClassWithPrintStackTrace.class);
            assertThrows(AssertionError.class, () -> CodingRules.NO_CLASS_SHOULD_ACCESS_STANDARD_STREAMS.check(javaClasses));
        }

        @Test
        void System_out_println_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(ClassWithSystemOut.class);
            assertThrows(AssertionError.class, () -> CodingRules.NO_CLASS_SHOULD_ACCESS_STANDARD_STREAMS.check(javaClasses));
        }

        @Test
        void class_without_access_to_standard_stream_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(ClassWithLog4J.class);
            assertDoesNotThrow(() -> CodingRules.NO_CLASS_SHOULD_ACCESS_STANDARD_STREAMS.check(javaClasses));
        }

        static class ClassWithPrintStackTrace {
            public ClassWithPrintStackTrace() {
                new Exception().printStackTrace();
            }
        }

        static class ClassWithSystemOut {
            public ClassWithSystemOut() {
                System.out.println("Hello World");
            }
        }
    }

    @Nested
    class NoLog4JTest {

        @Test
        void usage_of_Log4J_should_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(ClassWithLog4J.class);
            assertThrows(AssertionError.class, () -> CodingRules.NO_CLASS_SHOULD_USE_LOG4J.check(javaClasses));
        }

        @Test
        void no_usage_of_Log4J_should_not_cause_violation() {
            JavaClasses javaClasses = classFileImporter.importClasses(ClassWithSystemOut.class);
            assertDoesNotThrow(() -> CodingRules.NO_CLASS_SHOULD_USE_LOG4J.check(javaClasses));
        }

        @SuppressWarnings("unused")
        static class ClassWithLog4J {
            private static final Logger log = LogManager.getLogger(ClassWithLog4J.class);
        }
    }
}