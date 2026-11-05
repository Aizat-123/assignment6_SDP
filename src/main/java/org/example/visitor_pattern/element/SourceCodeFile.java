package org.example.visitor_pattern.element;

import org.example.visitor_pattern.visitor.ProjectVisitor;

public class SourceCodeFile implements ProjectElement {
    private final String name;
    private final int linesOfCode;
    private final int complexity;
    private final boolean hardcodedCredentials;
    private final boolean highRisk;

    public SourceCodeFile(String name, int linesOfCode, int complexity,
                          boolean hardcodedCredentials, boolean highRisk) {
        this.name = name;
        this.linesOfCode = linesOfCode;
        this.complexity = complexity;
        this.hardcodedCredentials = hardcodedCredentials;
        this.highRisk = highRisk;
    }

    public String getName() {
        return name;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getComplexity() {
        return complexity;
    }

    public boolean hasHardcodedCredentials() {
        return hardcodedCredentials;
    }

    public boolean isHighRisk() {
        return highRisk;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}