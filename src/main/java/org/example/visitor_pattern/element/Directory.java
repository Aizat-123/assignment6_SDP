package org.example.visitor_pattern.element;

import java.util.ArrayList;
import java.util.List;
import org.example.visitor_pattern.visitor.ProjectVisitor;

public class Directory implements ProjectElement {
    private final String name;
    private final List<ProjectElement> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(ProjectElement element) {
        children.add(element);
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);

        for (ProjectElement child : children) {
            child.accept(visitor);
        }
    }
}