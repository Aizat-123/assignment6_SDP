package org.example.visitor_pattern;

import java.util.List;
import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.Dependency;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;
import org.example.visitor_pattern.visitor.SecurityAuditVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityAuditVisitorTest {

    @Test
    void detectsSourceCodeProblems() {
        SourceCodeFile file = new SourceCodeFile(
                "PaymentService.java", 120, 15, true, true
        );
        SecurityAuditVisitor visitor = new SecurityAuditVisitor();

        file.accept(visitor);

        assertEquals(List.of(
                "PaymentService.java: hardcoded credentials",
                "PaymentService.java: high-risk source file"
        ), visitor.getIssues());
        assertEquals(2, visitor.getCriticalIssueCount());
    }

    @Test
    void detectsConfigurationProblems() {
        ConfigurationFile file = new ConfigurationFile(
                "application.properties", true, true
        );
        SecurityAuditVisitor visitor = new SecurityAuditVisitor();

        file.accept(visitor);

        assertEquals(List.of(
                "application.properties: exposed credentials",
                "application.properties: insecure configuration"
        ), visitor.getIssues());
    }

    @Test
    void reportsOnlyVulnerableDependencies() {
        DependencyFile file = new DependencyFile(
                "pom.xml",
                List.of(
                        new Dependency("safe-library", "1.0", false, false),
                        new Dependency("unsafe-library", "2.0", true, false)
                )
        );
        SecurityAuditVisitor visitor = new SecurityAuditVisitor();

        file.accept(visitor);

        assertEquals(List.of(
                "pom.xml: vulnerable dependency unsafe-library"
        ), visitor.getIssues());
        assertEquals(1, visitor.getCriticalIssueCount());
    }

    @Test
    void passesProjectWithoutSecurityProblems() {
        Directory project = new Directory("safe-project");

        project.add(new SourceCodeFile(
                "UserService.java", 80, 4, false, false
        ));
        project.add(new TestFile(
                "UserServiceTest.java", 40, 3
        ));
        project.add(new ConfigurationFile(
                "application.properties", false, false
        ));
        project.add(new DependencyFile(
                "pom.xml",
                List.of(new Dependency("safe-library", "1.0", false, false))
        ));

        SecurityAuditVisitor visitor = new SecurityAuditVisitor();

        project.accept(visitor);

        assertEquals(List.of(), visitor.getIssues());
        assertEquals(0, visitor.getCriticalIssueCount());
        assertEquals(
                "Security audit: PASS\nCritical issues: 0",
                visitor.getReport()
        );
    }

    @Test
    void checksFilesInsideNestedDirectoriesOnce() {
        Directory project = new Directory("project");
        Directory src = new Directory("src");
        Directory payment = new Directory("payment");

        payment.add(new SourceCodeFile(
                "PaymentService.java", 100, 8, true, false
        ));
        src.add(payment);
        project.add(src);

        SecurityAuditVisitor visitor = new SecurityAuditVisitor();

        project.accept(visitor);

        assertEquals(List.of(
                "PaymentService.java: hardcoded credentials"
        ), visitor.getIssues());
        assertEquals(1, visitor.getCriticalIssueCount());
    }

    @Test
    void passesEmptyDirectory() {
        Directory directory = new Directory("empty");
        SecurityAuditVisitor visitor = new SecurityAuditVisitor();

        directory.accept(visitor);

        assertEquals(0, visitor.getCriticalIssueCount());
        assertEquals(
                "Security audit: PASS\nCritical issues: 0",
                visitor.getReport()
        );
    }

    @Test
    void generatesFailedSecurityReport() {
        SourceCodeFile file = new SourceCodeFile(
                "PaymentService.java", 100, 8, true, false
        );
        SecurityAuditVisitor visitor = new SecurityAuditVisitor();

        file.accept(visitor);

        assertEquals(
                "Security audit: FAIL\n"
                        + "Critical issues: 1\n"
                        + "PaymentService.java: hardcoded credentials",
                visitor.getReport()
        );
    }
}