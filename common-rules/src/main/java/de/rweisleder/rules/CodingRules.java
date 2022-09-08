package de.rweisleder.rules;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class CodingRules {

    @ArchTest
    public static final ArchRule NO_CLASS_SHOULD_ACCESS_STANDARD_STREAMS = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    public static final ArchRule NO_CLASS_SHOULD_USE_LOG4J =
            noClasses()
                    .should().dependOnClassesThat()
                    .resideInAPackage("org.apache.logging.log4j..")
                    .as("classes should not depend on Log4J");

    @ArchTest
    public static final ArchRule NO_CLASS_SHOULD_USE_JACKSON_V1 =
            noClasses()
                    .should().dependOnClassesThat()
                    .resideInAPackage("org.codehaus.jackson..")
                    .as("classes should not depend on Jackson v1 (org.codehaus.jackson)")
                    .because("Jackson v2 (com.fasterxml.jackson) should be used instead");
}
