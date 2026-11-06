package org.example.visitor_pattern;

import java.util.List;
import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.Dependency;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.visitor.ComplianceVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplianceVisitorTest {

    @Test
    void reportsCredentialsInSourceAndConfiguration() {
        Directory project = new Directory("project");
        project.add(new SourceCodeFile(
                "PaymentService.java", 100, 5, true, false
        ));
        project.add(new ConfigurationFile(
                "application.properties", true, false
        ));
        ComplianceVisitor visitor = new ComplianceVisitor();

        project.accept(visitor);

        assertEquals(List.of(
                "PaymentService.java: hardcoded credentials",
                "application.properties: exposed credentials"
        ), visitor.getViolations());
    }

    @Test
    void reportsMissingAndBlankVersions() {
        DependencyFile file = new DependencyFile("pom.xml", List.of(
                new Dependency("library-a", null, false, false),
                new Dependency("library-b", "   ", false, false)
        ));
        ComplianceVisitor visitor = new ComplianceVisitor();

        file.accept(visitor);

        assertEquals(List.of(
                "pom.xml: library-a has no version",
                "pom.xml: library-b has no version"
        ), visitor.getViolations());
    }

    @Test
    void reportsBothViolationsForProhibitedDependencyWithoutVersion() {
        DependencyFile file = new DependencyFile("pom.xml", List.of(
                new Dependency("banned-library", "", false, true)
        ));
        ComplianceVisitor visitor = new ComplianceVisitor();

        file.accept(visitor);

        assertEquals(
                "Compliance: FAIL\n"
                        + "pom.xml: banned-library has no version\n"
                        + "pom.xml: banned-library is prohibited",
                visitor.getReport()
        );
    }

    @Test
    void passesProjectThatFollowsAllRules() {
        Directory project = new Directory("project");
        project.add(new SourceCodeFile(
                "UserService.java", 80, 4, false, false
        ));
        project.add(new ConfigurationFile(
                "application.properties", false, false
        ));
        project.add(new DependencyFile("pom.xml", List.of(
                new Dependency("allowed-library", "1.0", false, false)
        )));
        ComplianceVisitor visitor = new ComplianceVisitor();

        project.accept(visitor);

        assertEquals(List.of(), visitor.getViolations());
        assertEquals("Compliance: PASS", visitor.getReport());
    }
}