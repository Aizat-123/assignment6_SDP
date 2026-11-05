package org.example.visitor_pattern.visitor;

import java.util.ArrayList;
import java.util.List;
import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.Dependency;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;

public class SecurityAuditVisitor implements ProjectVisitor {
    private final List<String> issues = new ArrayList<>();

    @Override
    public void visit(SourceCodeFile file) {
        if (file.hasHardcodedCredentials()) {
            issues.add(file.getName() + ": hardcoded credentials");
        }

        if (file.isHighRisk()) {
            issues.add(file.getName() + ": high-risk source file");
        }
    }

    @Override
    public void visit(TestFile file) {
        // Для тестовых файлов правила безопасности пока не заданы.
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.hasExposedCredentials()) {
            issues.add(file.getName() + ": exposed credentials");
        }

        if (file.isInsecure()) {
            issues.add(file.getName() + ": insecure configuration");
        }
    }

    @Override
    public void visit(DependencyFile file) {
        for (Dependency dependency : file.getDependencies()) {
            if (dependency.isVulnerable()) {
                issues.add(file.getName()
                        + ": vulnerable dependency "
                        + dependency.getName());
            }
        }
    }

    @Override
    public void visit(Directory directory) {
        // Обход содержимого уже выполняется в Directory.accept().
    }

    public List<String> getIssues() {
        return List.copyOf(issues);
    }

    public int getCriticalIssueCount() {
        return issues.size();
    }

    public String getReport() {
        if (issues.isEmpty()) {
            return "Security audit: PASS\nCritical issues: 0";
        }

        return "Security audit: FAIL\n"
                + "Critical issues: " + getCriticalIssueCount()
                + "\n" + String.join("\n", issues);
    }
}