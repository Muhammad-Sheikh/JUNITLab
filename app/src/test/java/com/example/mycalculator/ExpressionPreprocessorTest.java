package com.example.mycalculator;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for ExpressionPreprocessor.preprocess(..).
 * Focus: keypad input shaping (spaces, symbol normalization, basic shape validation).
 * These tests are distinct from arithmetic correctness tests that target ExpressionEvaluator.
 */
public class ExpressionPreprocessorTest {

    /** Shorthand helper to keep test bodies readable. */
    private String p(String in) throws Exception {
        return ExpressionPreprocessor.preprocess(in);
    }

    // ------------------------------------------------------------------------
    // SUCCESSFUL CASES (input is accepted and normalized)
    // ------------------------------------------------------------------------

    @Test
    public void trimsSpacesAndNormalizesSymbols() throws Exception {
        // Spaces are removed and '×'/'÷' are normalized to '*'/'/'.
        // Result is ready for evaluation.
        assertEquals("7*8+9/3", p(" 7  ×  8  +  9 ÷ 3 "));
    }

    @Test
    public void preservesParenthesesAndDecimals_ifAvailable() throws Exception {
        // Parentheses and decimals are preserved (if the calculator allows them).
        assertEquals("(12.30-0.5)*2", p(" (12.30  -  0.5) × 2 "));
    }

    @Test
    public void allowsUnaryMinusAtStart() throws Exception {
        // Leading '-' is permitted for negative numbers.
        assertEquals("-0.75*4", p("-0.75 × 4"));
    }

    @Test
    public void leavesAlreadyNormalizedTextUntouched() throws Exception {
        // Text that is already clean should pass through unchanged.
        assertEquals("3+4*2-10/5", p("3+4*2-10/5"));
    }

    @Test
    public void removesMixedWhitespace_ifAvailable() throws Exception {
        // Tabs and multiple spaces are removed uniformly (if the calculator allows them).
        assertEquals("2*(3+4)", p("2 \t *  (3  + 4)"));
    }

    @Test
    public void nestedParenthesesRemain_ifAvailable() throws Exception {
        // Nested parentheses remain intact (if the calculator allows them).
        assertEquals("(2+(3-1))*4", p(" ( 2 + (3 - 1) ) × 4 "));
    }

    @Test
    public void fractionStyleRendersNumeratorSlashDenominator_ifAvailable() throws Exception {
        // If fraction-like rendering is enabled, a single top-level "a ÷ b" is displayed as "(a)/(b)".
        assertEquals("(1)/(2)", p("1 ÷ 2"));
        assertEquals("(8)/(0.25)", p(" 8 ÷ 0.25 "));
    }

    // ------------------------------------------------------------------------
    // UNSUCCESSFUL CASES (input is rejected with an exception)
    // ------------------------------------------------------------------------

    @Test(expected = Exception.class)
    public void emptyExpressionIsInvalid() throws Exception {
        // No input is not meaningful for evaluation.
        p("");
    }

    @Test(expected = Exception.class)
    public void spacesOnlyIsInvalid() throws Exception {
        // Whitespace alone is treated as empty.
        p("    \t  ");
    }

    @Test(expected = Exception.class)
    public void equalsSignIsRejected() throws Exception {
        // '=' is an execution key in the UI, not a character in expression text.
        p("2=3");
    }

    @Test(expected = Exception.class)
    public void leadingIllegalOperatorIsRejected() throws Exception {
        // Leading '+', '*', '/' are not permitted (leading '-' is allowed).
        p("+2");
    }

    @Test(expected = Exception.class)
    public void trailingOperatorIsRejected() throws Exception {
        // Expression cannot end with an operator or a lone dot.
        p("9+");
    }

    @Test(expected = Exception.class)
    public void malformedDecimalIsRejected() throws Exception {
        // Multiple dots within a single number are not allowed.
        p("3.1.4+2");
    }
}
