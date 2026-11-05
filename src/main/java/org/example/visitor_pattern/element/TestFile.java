package org.example.visitor_pattern.element;

import org.example.visitor_pattern.visitor.ProjectVisitor;

public class TestFile implements ProjectElement {
    private final String name;
    private final int linesOfCode;
    private final int testCount;

    public TestFile(String name, int linesOfCode, int testCount) {
        this.name = name;
        this.linesOfCode = linesOfCode;
        this.testCount = testCount;
    }

    public String getName() {
        return name;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getTestCount() {
        return testCount;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}