package de.rweisleder.rules;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;

public class AllRules {

    @ArchTest
    public static final ArchTests CODING_RULES = ArchTests.in(CodingRules.class);

    @ArchTest
    public static final ArchTests SERVICE_RULES = ArchTests.in(ServiceRules.class);

    @ArchTest
    public static final ArchTests PERSISTENCE_RULES = ArchTests.in(PersistenceRules.class);

    @ArchTest
    public static final ArchTests JPA_RULES = ArchTests.in(JpaRules.class);
}
