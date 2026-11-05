package org.example.visitor_pattern.element;

import org.example.visitor_pattern.visitor.ProjectVisitor;

public class ConfigurationFile implements ProjectElement {
    private final String name;
    private final boolean exposedCredentials;
    private final boolean insecure;

    public ConfigurationFile(String name, boolean exposedCredentials,
                             boolean insecure) {
        this.name = name;
        this.exposedCredentials = exposedCredentials;
        this.insecure = insecure;
    }

    public String getName() {
        return name;
    }

    public boolean hasExposedCredentials() {
        return exposedCredentials;
    }

    public boolean isInsecure() {
        return insecure;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}