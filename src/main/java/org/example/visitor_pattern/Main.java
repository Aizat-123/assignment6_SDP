package org.example.visitor_pattern;

import java.util.List;
import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.Dependency;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;
import org.example.visitor_pattern.visitor.ComplianceVisitor;
import org.example.visitor_pattern.visitor.ProjectStatisticsVisitor;
import org.example.visitor_pattern.visitor.QualityGateVisitor;
import org.example.visitor_pattern.visitor.SecurityAuditVisitor;

public class Main {

    public static void main(String[] args) {
        Directory project = createDemoProject();

        SecurityAuditVisitor security = new SecurityAuditVisitor();
        ProjectStatisticsVisitor statistics = new ProjectStatisticsVisitor();
        QualityGateVisitor qualityGate = new QualityGateVisitor();
        ComplianceVisitor compliance = new ComplianceVisitor();

        project.accept(security);
        project.accept(statistics);
        project.accept(qualityGate);
        project.accept(compliance);

        System.out.println("Project: " + project.getName());
        System.out.println();
        System.out.println(security.getReport());
        System.out.println();
        System.out.println(statistics.getReport());
        System.out.println();
        System.out.println(qualityGate.getReport());
        System.out.println();
        System.out.println(compliance.getReport());
    }

    private static Directory createDemoProject() {
        Directory project = new Directory("fintech-project");

        Directory src = new Directory("src");
        src.add(new SourceCodeFile(
                "PaymentService.java", 120, 12, true, false
        ));
        src.add(new SourceCodeFile(
                "UserService.java", 80, 6, false, false
        ));

        Directory tests = new Directory("tests");
        tests.add(new TestFile("PaymentServiceTest.java", 50, 1));

        Directory config = new Directory("config");
        config.add(new ConfigurationFile(
                "application.properties", false, true
        ));

        project.add(src);
        project.add(tests);
        project.add(config);
        project.add(new DependencyFile("pom.xml", List.of(
                new Dependency("payment-sdk", "1.2.0", false, false),
                new Dependency("legacy-crypto", "", true, true)
        )));

        return project;
    }
}