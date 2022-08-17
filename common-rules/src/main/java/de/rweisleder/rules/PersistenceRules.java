package de.rweisleder.rules;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.equivalentTo;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.codeUnits;

public class PersistenceRules {

    @ArchTest
    public static final ArchRule TRANSACTIONAL_ACCESS_TO_ENTITY_MANAGER =
            codeUnits()
                    .that(accessClass(equivalentTo(EntityManager.class)))
                    .should().beAnnotatedWith(Transactional.class)
                    .orShould().beDeclaredInClassesThat().areAnnotatedWith(Transactional.class);

    private static DescribedPredicate<? super JavaCodeUnit> accessClass(DescribedPredicate<JavaClass> targetClass) {
        return new DescribedPredicate<>("access class " + targetClass.getDescription()) {
            @Override
            public boolean test(JavaCodeUnit javaCodeUnit) {
                for (JavaAccess<?> javaAccess : javaCodeUnit.getAccessesFromSelf()) {
                    JavaClass targetOwner = javaAccess.getTargetOwner();
                    return targetClass.test(targetOwner);
                }

                return false;
            }
        };
    }
}
