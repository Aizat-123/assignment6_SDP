package org.example.visitor_pattern.element;

import java.util.List;
import org.example.visitor_pattern.visitor.ProjectVisitor;

public class DependencyFile implements ProjectElement {
    private final String name;
    private final List<Dependency> dependencies;

    public DependencyFile(String name, List<Dependency> dependencies) {
        this.name = name;
        this.dependencies = List.copyOf(dependencies);
    }

    public String getName() {
        return name;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}