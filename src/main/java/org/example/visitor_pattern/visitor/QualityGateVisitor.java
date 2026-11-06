package org.example.visitor_pattern.visitor;

import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;

public class QualityGateVisitor implements ProjectVisitor {
    private final SecurityAuditVisitor security = new SecurityAuditVisitor();
    private final ProjectStatisticsVisitor statistics =
            new ProjectStatisticsVisitor();

    @Override
    public void visit(SourceCodeFile file) {
        security.visit(file);
        statistics.visit(file);
    }

    @Override
    public void visit(TestFile file) {
        security.visit(file);
        statistics.visit(file);
    }

    @Override
    public void visit(ConfigurationFile file) {
        security.visit(file);
        statistics.visit(file);
    }

    @Override
    public void visit(DependencyFile file) {
        security.visit(file);
        statistics.visit(file);
    }

    @Override
    public void visit(Directory directory) {
        // Обход дочерних элементов выполняет Directory.accept().
    }

    public boolean isSecurityPassed() {
        return security.getCriticalIssueCount() == 0;
    }

    public boolean isComplexityPassed() {
        return statistics.getAverageComplexity() <= 10;
    }

    public boolean isTestsPassed() {
        return statistics.getTestSourceRatio() >= 0.40;
    }

    public boolean isPassed() {
        return isSecurityPassed()
                && isComplexityPassed()
                && isTestsPassed();
    }

    public String getReport() {
        return "Security: " + status(isSecurityPassed())
                + "\nComplexity: " + status(isComplexityPassed())
                + "\nTests: " + status(isTestsPassed())
                + "\n\nFINAL RESULT: " + (isPassed() ? "PASSED" : "FAILED");
    }

    private String status(boolean passed) {
        return passed ? "PASS" : "FAIL";
    }
}