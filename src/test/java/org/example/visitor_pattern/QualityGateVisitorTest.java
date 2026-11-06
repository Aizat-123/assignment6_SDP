package org.example.visitor_pattern;

import java.util.List;
import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.Dependency;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;
import org.example.visitor_pattern.visitor.QualityGateVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QualityGateVisitorTest {

    @Test
    void passesAtExactComplexityAndTestRatioLimits() {
        Directory project = createProject(10, 2);
        QualityGateVisitor visitor = new QualityGateVisitor();

        project.accept(visitor);

        assertTrue(visitor.isSecurityPassed());
        assertTrue(visitor.isComplexityPassed());
        assertTrue(visitor.isTestsPassed());
        assertTrue(visitor.isPassed());
        assertEquals(
                "Security: PASS\n"
                        + "Complexity: PASS\n"
                        + "Tests: PASS\n\n"
                        + "FINAL RESULT: PASSED",
                visitor.getReport()
        );
    }

    @Test
    void failsWhenComplexityIsTooHigh() {
        Directory project = createProject(11, 2);
        QualityGateVisitor visitor = new QualityGateVisitor();

        project.accept(visitor);

        assertTrue(visitor.isSecurityPassed());
        assertFalse(visitor.isComplexityPassed());
        assertTrue(visitor.isTestsPassed());
        assertFalse(visitor.isPassed());
    }

    @Test
    void failsWhenThereAreNotEnoughTests() {
        Directory project = createProject(5, 1);
        QualityGateVisitor visitor = new QualityGateVisitor();

        project.accept(visitor);

        assertTrue(visitor.isSecurityPassed());
        assertTrue(visitor.isComplexityPassed());
        assertFalse(visitor.isTestsPassed());
        assertFalse(visitor.isPassed());
        assertEquals(
                "Security: PASS\n"
                        + "Complexity: PASS\n"
                        + "Tests: FAIL\n\n"
                        + "FINAL RESULT: FAILED",
                visitor.getReport()
        );
    }

    @Test
    void failsWhenConfigurationExposesCredentials() {
        Directory project = createProject(5, 2);
        project.add(new ConfigurationFile(
                "application.properties", true, false
        ));
        QualityGateVisitor visitor = new QualityGateVisitor();

        project.accept(visitor);

        assertFalse(visitor.isSecurityPassed());
        assertTrue(visitor.isComplexityPassed());
        assertTrue(visitor.isTestsPassed());
        assertFalse(visitor.isPassed());
    }

    @Test
    void failsWhenDependencyIsVulnerable() {
        Directory project = createProject(5, 2);
        project.add(new DependencyFile(
                "pom.xml",
                List.of(new Dependency("unsafe-library", "1.0", true, false))
        ));
        QualityGateVisitor visitor = new QualityGateVisitor();

        project.accept(visitor);

        assertFalse(visitor.isSecurityPassed());
        assertFalse(visitor.isPassed());
    }

    @Test
    void failsWhenSourceFileHasHardcodedCredentials() {
        Directory project = new Directory("project");

        project.add(new SourceCodeFile(
                "PaymentService.java", 100, 5, true, false
        ));
        project.add(new TestFile(
                "PaymentServiceTest.java", 30, 1
        ));

        QualityGateVisitor visitor = new QualityGateVisitor();

        project.accept(visitor);

        assertFalse(visitor.isSecurityPassed());
        assertTrue(visitor.isComplexityPassed());
        assertTrue(visitor.isTestsPassed());
        assertFalse(visitor.isPassed());
    }

    @Test
    void failsEmptyProjectBecauseItHasNoTests() {
        Directory project = new Directory("empty");
        QualityGateVisitor visitor = new QualityGateVisitor();

        project.accept(visitor);

        assertTrue(visitor.isSecurityPassed());
        assertTrue(visitor.isComplexityPassed());
        assertFalse(visitor.isTestsPassed());
        assertFalse(visitor.isPassed());
    }

    private Directory createProject(int complexity, int testCount) {
        Directory project = new Directory("project");

        for (int i = 1; i <= 5; i++) {
            project.add(new SourceCodeFile(
                    "Service" + i + ".java",
                    100,
                    complexity,
                    false,
                    false
            ));
        }

        project.add(new TestFile("ServiceTest.java", 50, testCount));

        return project;
    }
}