package de.rweisleder.example.users;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;
import de.rweisleder.rules.AllRules;

@AnalyzeClasses(importOptions = DoNotIncludeTests.class)
public class ArchitectureTest {

    @ArchTest
    public static final ArchTests allCommonRules = ArchTests.in(AllRules.class);

}
