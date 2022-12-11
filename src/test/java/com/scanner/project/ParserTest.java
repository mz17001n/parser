package com.scanner.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class ParserTest {

    /*
     * sets the TokenStream to analyze the relevant kay file
     */
    private Program setup(int testNum) {
        TokenStream tStream = new TokenStream("src/test/java/com/scanner/project/ptest" + testNum + ".kay");

		ConcreteSyntax cSyntax = new ConcreteSyntax(tStream);
		Program p = cSyntax.program();
		return p;
    }

    @Test
    public void integerAssignment() {
        Program p = setup(0);
        String expected = "\nAbstract syntax of the KAY Program: \n  com.scanner.project.Declarations: \n    Declarations = {<i, integer>}\n  com.scanner.project.Block: \n    com.scanner.project.Assignment: \n      com.scanner.project.Variable: i\n      com.scanner.project.Value: 7\n".replaceAll("\\n", System.getProperty("line.separator"));
        assertEquals(expected, p.display());
    }

    @Test
    public void exceptionWhenProgramStartsWithVoid() {
        try {
            setup(1);
            System.err.println("Exception should have been thrown!");
            fail();
        } catch (Exception e) {
            assertEquals("Syntax error - Expecting: main But saw: Identifier = void", e.getMessage());
        }
    }

    @Test
    public void exceptionWhenProgramHasParenthesesAfterMainBeforeBracket() {
        try {
            setup(2);
            System.err.println("Exception should have been thrown!");
            fail();
        } catch (Exception e) {
            assertEquals("Syntax error - Expecting: { But saw: Separator = (", e.getMessage());
        }
    }

    @Test
    public void exceptionWhenIntIsUsedInteadOfInteger() {
        try {
            setup(3);
            System.err.println("Exception should have been thrown!");
            fail();
        } catch (Exception e) {
            assertEquals("Syntax error - Expecting: := But saw: Identifier = i", e.getMessage());
        }
    }

    @Test
    public void exceptionWhenEqualsOperatorIsMissingColon() {
        try {
            setup(4);
            System.err.println("Exception should have been thrown!");
            fail();
        } catch (Exception e) {
            assertEquals("Syntax error - Expecting: := But saw: Other = =", e.getMessage());
        }
    }

    @Test
    public void doublePipeOperatorInAssignment() {
        Program p = setup(5);
        String expected = "\nAbstract syntax of the KAY Program: \n  com.scanner.project.Declarations: \n    Declarations = {<b1, bool>, <b2, bool>, <b3, bool>}\n  com.scanner.project.Block: \n    com.scanner.project.Assignment: \n      com.scanner.project.Variable: b3\n      com.scanner.project.Binary: \n        com.scanner.project.Operator: ||\n        com.scanner.project.Variable: b1\n        com.scanner.project.Variable: b2\n".replaceAll("\\n", System.getProperty("line.separator"));
        assertEquals(expected, p.display());
    }

    @Test
    public void ifElseStatement() {
        Program p = setup(6);
        String expected = "\nAbstract syntax of the KAY Program: \n  com.scanner.project.Declarations: \n    Declarations = {<b1, bool>, <b2, bool>}\n  com.scanner.project.Block: \n    com.scanner.project.Assignment: \n      com.scanner.project.Variable: b1\n      com.scanner.project.Value: true\n    com.scanner.project.Conditional: \n      com.scanner.project.Variable: b1\n      com.scanner.project.Block: \n        com.scanner.project.Assignment: \n          com.scanner.project.Variable: b2\n          com.scanner.project.Value: true\n      com.scanner.project.Block: \n        com.scanner.project.Assignment: \n          com.scanner.project.Variable: b2\n          com.scanner.project.Value: false\n".replaceAll("\\n", System.getProperty("line.separator"));
        assertEquals(expected, p.display());
    }

    @Test
    public void whileStatement() {
        Program p = setup(7);
        String expected = "\nAbstract syntax of the KAY Program: \n  com.scanner.project.Declarations: \n    Declarations = {<i, integer>, <n, integer>}\n  com.scanner.project.Block: \n    com.scanner.project.Assignment: \n      com.scanner.project.Variable: n\n      com.scanner.project.Value: 10\n    com.scanner.project.Loop: \n      com.scanner.project.Binary: \n        com.scanner.project.Operator: <\n        com.scanner.project.Variable: i\n        com.scanner.project.Variable: n\n      com.scanner.project.Block: \n        com.scanner.project.Assignment: \n          com.scanner.project.Variable: i\n          com.scanner.project.Binary: \n            com.scanner.project.Operator: +\n            com.scanner.project.Variable: i\n            com.scanner.project.Value: 1\n".replaceAll("\\n", System.getProperty("line.separator"));
        assertEquals(expected, p.display());
    }

    @Test
    public void secretTest1() {
        Program p = setup(8);
        String expected = "\nAbstract syntax of the KAY Program: \n  com.scanner.project.Declarations: \n    Declarations = {<x, integer>, <y, integer>, <max, integer>}\n  com.scanner.project.Block: \n    com.scanner.project.Conditional: \n      com.scanner.project.Binary: \n        com.scanner.project.Operator: >\n        com.scanner.project.Variable: x\n        com.scanner.project.Variable: y\n      com.scanner.project.Block: \n        com.scanner.project.Assignment: \n          com.scanner.project.Variable: max\n          com.scanner.project.Variable: x\n      com.scanner.project.Block: \n        com.scanner.project.Assignment: \n          com.scanner.project.Variable: max\n          com.scanner.project.Variable: y\n".replaceAll("\\n", System.getProperty("line.separator"));
        assertEquals(expected, p.display());
    }

    @Test
    public void secretTest2() {
        try {
            setup(9);
            System.err.println("Exception should have been thrown!");
            fail();
        } catch (Exception e) {
            assertEquals("Syntax error - Expecting: ; But saw: Identifier = i", e.getMessage());
        }
    }

    @Test
    public void secretTest3() {
        assertThrows(RuntimeException.class, () -> setup(10));
    }
    
    @Test
    public void secretTest4() {
        try {
            setup(11);
            System.err.println("Exception should have been thrown!");
            fail();
        } catch (Exception e) {
            assertEquals("Syntax error - Expecting: ( But saw: Separator = {", e.getMessage());
        }
    }
}
