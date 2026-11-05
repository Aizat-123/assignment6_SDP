package org.example.visitor_pattern;

import java.util.List;
import org.example.visitor_pattern.element.ConfigurationFile;
import org.example.visitor_pattern.element.Dependency;
import org.example.visitor_pattern.element.DependencyFile;
import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.element.TestFile;
import org.example.visitor_pattern.visitor.ProjectStatisticsVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectStatisticsVisitorTest {

    @Test
    void calculatesStatisticsForDifferentFileTypes() {
        Directory project = new Directory("fintech-project");

        project.add(new SourceCodeFile(
                "PaymentService.java", 120, 12, false, false
        ));
        project.add(new SourceCodeFile(
                "UserService.java", 80, 6, false, false
        ));
        project.add(new TestFile(
                "PaymentServiceTest.java", 50, 3
        ));
        project.add(new ConfigurationFile(
                "application.properties", false, false
        ));
        project.add(new DependencyFile(
                "pom.xml",
                List.of(
                        new Dependency("library-a", "1.0", false, false),
                        new Dependency("library-b", "2.0", false, false)
                )
        ));

        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();

        project.accept(visitor);

        assertEquals(5, visitor.getTotalFiles());
        assertEquals(250, visitor.getTotalLinesOfCode());
        assertEquals(2, visitor.getSourceFileCount());
        assertEquals(3, visitor.getTestCount());
        assertEquals(2, visitor.getDependencyCount());
        assertEquals(9.0, visitor.getAverageComplexity(), 0.0001);
        assertEquals(1.5, visitor.getTestSourceRatio(), 0.0001);
    }

    @Test
    void calculatesFractionalAverageComplexity() {
        Directory project = new Directory("project");

        project.add(new SourceCodeFile(
                "First.java", 10, 4, false, false
        ));
        project.add(new SourceCodeFile(
                "Second.java", 20, 5, false, false
        ));

        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();

        project.accept(visitor);

        assertEquals(4.5, visitor.getAverageComplexity(), 0.0001);
    }

    @Test
    void calculatesFractionalTestSourceRatio() {
        Directory project = new Directory("project");

        project.add(new SourceCodeFile(
                "First.java", 10, 2, false, false
        ));
        project.add(new SourceCodeFile(
                "Second.java", 20, 3, false, false
        ));
        project.add(new TestFile(
                "FirstTest.java", 15, 1
        ));

        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();

        project.accept(visitor);

        assertEquals(0.5, visitor.getTestSourceRatio(), 0.0001);
    }

    @Test
    void countsFilesInsideNestedDirectoriesOnce() {
        Directory project = new Directory("project");
        Directory src = new Directory("src");
        Directory payment = new Directory("payment");

        payment.add(new SourceCodeFile(
                "PaymentService.java", 100, 7, false, false
        ));
        src.add(payment);
        project.add(src);

        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();

        project.accept(visitor);

        assertEquals(1, visitor.getTotalFiles());
        assertEquals(1, visitor.getSourceFileCount());
        assertEquals(100, visitor.getTotalLinesOfCode());
        assertEquals(7.0, visitor.getAverageComplexity(), 0.0001);
    }

    @Test
    void returnsZeroStatisticsForEmptyDirectory() {
        Directory project = new Directory("empty");
        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();

        project.accept(visitor);

        assertEquals(0, visitor.getTotalFiles());
        assertEquals(0, visitor.getTotalLinesOfCode());
        assertEquals(0, visitor.getSourceFileCount());
        assertEquals(0, visitor.getTestCount());
        assertEquals(0, visitor.getDependencyCount());
        assertEquals(0.0, visitor.getAverageComplexity(), 0.0001);
        assertEquals(0.0, visitor.getTestSourceRatio(), 0.0001);
    }

    @Test
    void handlesTestsWithoutSourceFiles() {
        TestFile file = new TestFile("ExampleTest.java", 30, 4);
        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();

        file.accept(visitor);

        assertEquals(1, visitor.getTotalFiles());
        assertEquals(30, visitor.getTotalLinesOfCode());
        assertEquals(4, visitor.getTestCount());
        assertEquals(0, visitor.getSourceFileCount());
        assertEquals(0.0, visitor.getAverageComplexity(), 0.0001);
        assertEquals(0.0, visitor.getTestSourceRatio(), 0.0001);
    }
}