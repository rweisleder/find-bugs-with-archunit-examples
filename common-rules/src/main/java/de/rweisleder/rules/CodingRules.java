package de.rweisleder.rules;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.conditions.ArchConditions.dependOnClassesThat;
import static com.tngtech.archunit.lang.conditions.ArchConditions.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class CodingRules {

    @ArchTest
    public static final ArchRule NO_CLASS_SHOULD_ACCESS_STANDARD_STREAMS = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    public static final ArchRule NO_CLASS_SHOULD_USE_LOG4J =
            classes()
                    .should(not(dependOnClassesThat(resideInAPackage("org.apache.logging.log4j.."))));

    @ArchTest
    public static final ArchRule NO_CLASS_SHOULD_USE_JACKSON_V1 =
            classes()
                    .should(not(dependOnClassesThat(resideInAPackage("org.codehaus.jackson.."))));
}
