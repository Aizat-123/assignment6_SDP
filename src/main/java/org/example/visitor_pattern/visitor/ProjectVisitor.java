package org.example.visitor_pattern.visitor;

import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;

public interface ProjectVisitor {
    void visit(SourceCodeFile file);

    void visit(TestFile file);

    void visit(ConfigurationFile file);

    void visit(DependencyFile file);

    void visit(Directory directory);
}