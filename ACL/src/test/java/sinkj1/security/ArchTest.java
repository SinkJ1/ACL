package sinkj1.security;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("sinkj1.security");

        noClasses()
            .that()
            .resideInAnyPackage("sinkj1.security.service..")
            .or()
            .resideInAnyPackage("sinkj1.security.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..sinkj1.security.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
