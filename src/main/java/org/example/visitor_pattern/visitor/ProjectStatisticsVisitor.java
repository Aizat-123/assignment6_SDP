package org.example.visitor_pattern.visitor;

import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;

public class ProjectStatisticsVisitor implements ProjectVisitor {
    private int totalFiles;
    private int totalLinesOfCode;
    private int sourceFileCount;
    private int testCount;
    private int dependencyCount;
    private int totalComplexity;

    @Override
    public void visit(SourceCodeFile file) {
        totalFiles++;
        sourceFileCount++;
        totalLinesOfCode += file.getLinesOfCode();
        totalComplexity += file.getComplexity();
    }

    @Override
    public void visit(TestFile file) {
        totalFiles++;
        totalLinesOfCode += file.getLinesOfCode();
        testCount += file.getTestCount();
    }

    @Override
    public void visit(ConfigurationFile file) {
        totalFiles++;
    }

    @Override
    public void visit(DependencyFile file) {
        totalFiles++;
        dependencyCount += file.getDependencies().size();
    }

    @Override
    public void visit(Directory directory) {
        // Папки не считаются файлами. Обход выполняет Directory.accept().
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    public int getTotalLinesOfCode() {
        return totalLinesOfCode;
    }

    public int getSourceFileCount() {
        return sourceFileCount;
    }

    public int getTestCount() {
        return testCount;
    }

    public int getDependencyCount() {
        return dependencyCount;
    }

    public double getAverageComplexity() {
        if (sourceFileCount == 0) {
            return 0;
        }

        return (double) totalComplexity / sourceFileCount;
    }

    public double getTestSourceRatio() {
        if (sourceFileCount == 0) {
            return 0;
        }

        return (double) testCount / sourceFileCount;
    }

    public String getReport() {
        return "Project statistics\n"
                + "Total files: " + totalFiles
                + "\nLines of code: " + totalLinesOfCode
                + "\nSource files: " + sourceFileCount
                + "\nTests: " + testCount
                + "\nDependencies: " + dependencyCount
                + "\nAverage complexity: " + getAverageComplexity()
                + "\nTest/source ratio: " + getTestSourceRatio();
    }
}