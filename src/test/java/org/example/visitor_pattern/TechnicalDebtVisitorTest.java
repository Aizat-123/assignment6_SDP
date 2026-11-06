package org.example.visitor_pattern;

import org.example.visitor_pattern.element.Directory;
import org.example.visitor_pattern.element.SourceCodeFile;
import org.example.visitor_pattern.visitor.TechnicalDebtVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TechnicalDebtVisitorTest {

    @Test
    void addsDebtFromMultipleSourceFiles() {
        Directory project = new Directory("project");
        Directory src = new Directory("src");

        src.add(new SourceCodeFile("PaymentService.java", 120, 12, false, false));
        src.add(new SourceCodeFile("UserService.java", 80, 15, false, false));
        src.add(new SourceCodeFile("Currency.java", 30, 6, false, false));
        project.add(src);

        TechnicalDebtVisitor visitor = new TechnicalDebtVisitor();

        project.accept(visitor);

        assertEquals(7, visitor.getDebtPoints());
        assertEquals("Technical debt: 7 point(s)", visitor.getReport());
    }

    @Test
    void addsNoDebtAtComplexityLimit() {
        SourceCodeFile file = new SourceCodeFile(
                "PaymentService.java", 120, 10, false, false
        );
        TechnicalDebtVisitor visitor = new TechnicalDebtVisitor();

        file.accept(visitor);

        assertEquals(0, visitor.getDebtPoints());
    }
}