package de.rweisleder.rules;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.core.domain.JavaAccess.Predicates.targetOwner;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.is;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.ProxyRules.no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with;

public class ServiceRules {

    @ArchTest
    public static final ArchRule SERVICE_CLASS_NAMES_SHOULD_HAVE_SUFFIX =
            classes()
                    .that().areAnnotatedWith(Service.class)
                    .should().haveSimpleNameEndingWith("Service");

    @ArchTest
    public static final ArchRule SERVICE_METHODS_SHOULD_LOG =
            methods()
                    .that().arePublic()
                    .and().areDeclaredInClassesThat().areAnnotatedWith(Service.class)
                    .should(callMethodWhere(targetOwner(is(assignableTo(Logger.class)))));

    @ArchTest
    public static final ArchRule SERVICES_SHOULD_BE_SECURED =
            methods()
                    .that().arePublic()
                    .and().areDeclaredInClassesThat().areAnnotatedWith(Service.class)
                    .should().beAnnotatedWith(Secured.class);

    @ArchTest
    public static final ArchRule SPRING_CACHEABLE_RULE =
            no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(Cacheable.class);

    private static ArchCondition<JavaMethod> callMethodWhere(final DescribedPredicate<? super JavaMethodCall> predicate) {
        return new ArchCondition<>("call method where " + predicate.getDescription()) {
            @Override
            public void check(JavaMethod javaMethod, ConditionEvents events) {
                boolean satisfied = false;
                for (JavaMethodCall methodCall : javaMethod.getMethodCallsFromSelf()) {
                    if (predicate.test(methodCall)) {
                        satisfied = true;
                        events.add(SimpleConditionEvent.satisfied(javaMethod, methodCall.getDescription()));
                    }
                }

                if (!satisfied) {
                    events.add(SimpleConditionEvent.violated(javaMethod, javaMethod.getDescription() + " does not call method where " + predicate.getDescription() + " " + javaMethod.getSourceCodeLocation()));
                }
            }
        };
    }

}
