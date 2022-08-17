package de.rweisleder.rules;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import javax.persistence.SequenceGenerator;
import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

public class JpaRules {

    @ArchTest
    public static final ArchRule NO_SEQUENCE_GENERATOR_ON_CLASS =
            classes()
                    .should().notBeAnnotatedWith(SequenceGenerator.class)
                    .because("@SequenceGenerator should be defined on field");

    @ArchTest
    public static final ArchRule SEQUENCE_GENERATOR_ALLOCATION_SIZE_RULE =
            fields()
                    .that().areAnnotatedWith(SequenceGenerator.class)
                    .should().beAnnotatedWith(
                            annotationWithProperty(SequenceGenerator.class, sg -> sg.allocationSize() == 1)
                                    .as("@SequenceGenerator(allocationSize = 1, ...)"));

    @SuppressWarnings("SameParameterValue")
    private static <T extends Annotation> DescribedPredicate<JavaAnnotation<?>> annotationWithProperty(Class<T> annotationClass, Predicate<T> annotationPredicate) {
        return new DescribedPredicate<>("annotation with property") {
            @Override
            public boolean test(JavaAnnotation<?> javaAnnotation) {
                if (javaAnnotation.getRawType().isEquivalentTo(annotationClass)) {
                    T annotation = javaAnnotation.as(annotationClass);
                    return annotationPredicate.test(annotation);
                }

                return false;
            }
        };
    }
}
