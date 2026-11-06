package org.example.visitor_pattern.visitor;

import java.util.ArrayList;
import java.util.List;
import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.Dependency;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;

public class ComplianceVisitor implements ProjectVisitor {
    private final List<String> violations = new ArrayList<>();

    @Override
    public void visit(SourceCodeFile file) {
        if (file.hasHardcodedCredentials()) {
            violations.add(file.getName() + ": hardcoded credentials");
        }
    }

    @Override
    public void visit(TestFile file) {
        // Выбранные правила не проверяют тестовые файлы.
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.hasExposedCredentials()) {
            violations.add(file.getName() + ": exposed credentials");
        }
    }

    @Override
    public void visit(DependencyFile file) {
        for (Dependency dependency : file.getDependencies()) {
            String location = file.getName() + ": " + dependency.getName();
            String version = dependency.getVersion();

            if (version == null || version.isBlank()) {
                violations.add(location + " has no version");
            }

            if (dependency.isProhibited()) {
                violations.add(location + " is prohibited");
            }
        }
    }

    @Override
    public void visit(Directory directory) {
        // Обход выполняет Directory.accept().
    }

    public List<String> getViolations() {
        return List.copyOf(violations);
    }

    public String getReport() {
        if (violations.isEmpty()) {
            return "Compliance: PASS";
        }

        return "Compliance: FAIL\n" + String.join("\n", violations);
    }
}