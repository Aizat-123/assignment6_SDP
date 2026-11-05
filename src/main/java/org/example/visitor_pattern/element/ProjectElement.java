package org.example.visitor_pattern.element;

import org.example.visitor_pattern.visitor.ProjectVisitor;

public interface ProjectElement {
    void accept(ProjectVisitor visitor);
}