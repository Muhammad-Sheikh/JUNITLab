package com.example.mycalculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for ExpressionEvaluator's evaluate function
 */
public class MainActivityTest {

    private String evaluate(String expression) throws Exception {
        return ExpressionEvaluator.evaluate(expression);
    }

    // Basic arithmetic operations tests
    @Test
    public void testSimpleAddition() throws Exception {
        assertEquals("5", evaluate("2+3"));
    }

    @Test
    public void testSimpleSubtraction() throws Exception {
        assertEquals("3", evaluate("5-2"));
    }

    @Test
    public void testSimpleMultiplication() throws Exception {
        assertEquals("6", evaluate("2*3"));
    }

    @Test
    public void testSimpleDivision() throws Exception {
        assertEquals("2", evaluate("6/3"));
    }

    // Tests with special symbols (× and ÷)
    @Test
    public void testMultiplicationWithSymbol() throws Exception {
        assertEquals("15", evaluate("3×5"));
    }

    @Test
    public void testDivisionWithSymbol() throws Exception {
        assertEquals("4", evaluate("12÷3"));
    }

    // Tests for decimal results
    @Test
    public void testDivisionWithDecimalResult() throws Exception {
        String result = evaluate("5/2");
        assertEquals("2.5", result);
    }

    @Test
    public void testDecimalMultiplication() throws Exception {
        String result = evaluate("2.5*4");
        assertEquals("10", result);
    }

    // Tests for complex expressions
    @Test
    public void testComplexExpression() throws Exception {
        assertEquals("14", evaluate("2+3*4"));
    }

    @Test
    public void testExpressionWithParentheses() throws Exception {
        assertEquals("20", evaluate("(2+3)*4"));
    }

    @Test
    public void testNestedParentheses() throws Exception {
        assertEquals("10", evaluate("((2+3)*4)/2"));
    }

    // Tests for negative numbers
    @Test
    public void testNegativeResult() throws Exception {
        assertEquals("-3", evaluate("2-5"));
    }

    @Test
    public void testNegativeMultiplication() throws Exception {
        assertEquals("-6", evaluate("2*-3"));
    }

    // Tests for edge cases
    @Test
    public void testZeroAddition() throws Exception {
        assertEquals("5", evaluate("5+0"));
    }

    @Test
    public void testZeroMultiplication() throws Exception {
        assertEquals("0", evaluate("5*0"));
    }

    @Test
    public void testDivisionByOne() throws Exception {
        assertEquals("5", evaluate("5/1"));
    }

    @Test(expected = Exception.class)
    public void testDivisionByZero() throws Exception {
        evaluate("5/0");
    }

    // Tests for floating point precision
    @Test
    public void testLongDecimalResult() throws Exception {
        String result = evaluate("10/3");
        // Should be truncated and trailing zeros removed
        assertTrue(result.startsWith("3.3333"));
    }

    @Test
    public void testIntegerResult() throws Exception {
        // When result is a whole number, it should not have decimal point
        assertEquals("10", evaluate("5*2"));
        assertEquals("8", evaluate("4+4"));
    }

    // Tests for order of operations
    @Test
    public void testOrderOfOperations() throws Exception {
        assertEquals("11", evaluate("5+2*3"));
    }

    @Test
    public void testOrderOfOperationsWithDivision() throws Exception {
        assertEquals("7", evaluate("10-6/2"));
    }

    @Test
    public void testMultipleOperations() throws Exception {
        assertEquals("11", evaluate("2+3*4-6/2"));
    }

    // Test with spaces (if the calculator allows them)
    @Test
    public void testExpressionWithSpaces() throws Exception {
        assertEquals("5", evaluate("2 + 3"));
    }

    // Test for large numbers
    @Test
    public void testLargeNumbers() throws Exception {
        assertEquals("1000000", evaluate("1000*1000"));
    }

    @Test
    public void testVeryLargeMultiplication() throws Exception {
        String result = evaluate("999999*999999");
        assertEquals("999998000001", result);
    }

    @Test(expected = Exception.class)
    public void testEmptyExpression() throws Exception {
        evaluate("");
    }

    @Test(expected = Exception.class)
    public void testInvalidCharacters() throws Exception {
        evaluate("2+abc");
    }
}
