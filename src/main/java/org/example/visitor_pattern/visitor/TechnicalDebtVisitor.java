package org.example.visitor_pattern.visitor;

import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;

public class TechnicalDebtVisitor implements ProjectVisitor {
    private static final int COMPLEXITY_LIMIT = 10;

    private int debtPoints;

    @Override
    public void visit(SourceCodeFile file) {
        int excess = file.getComplexity() - COMPLEXITY_LIMIT;
        debtPoints += Math.max(0, excess);
    }

    @Override
    public void visit(TestFile file) {
    }

    @Override
    public void visit(ConfigurationFile file) {
    }

    @Override
    public void visit(DependencyFile file) {
    }

    @Override
    public void visit(Directory directory) {
    }

    public int getDebtPoints() {
        return debtPoints;
    }

    public String getReport() {
        return "Technical debt: " + debtPoints + " point(s)";
    }
}
