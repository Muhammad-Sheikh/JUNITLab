package com.example.mycalculator;

/**
 * Prepares keypad text for evaluation:
 * - Removes whitespace.
 * - Converts '×' → '*' and '÷' → '/'.
 * - Optionally renders a top-level "a/b" as "(a)/(b)" for fraction-style display.
 * - Enforces a basic well-formed shape (no '=' in text, no illegal starts/ends, no malformed decimals).
 *
 * This class is separate from arithmetic evaluation.
 */
public final class ExpressionPreprocessor {

    private ExpressionPreprocessor() {}

    /**
     * @param in raw keypad text
     * @return normalized expression text, ready for evaluation
     * @throws IllegalArgumentException if the text is not suitable for evaluation
     */
    public static String preprocess(String in) {
        // 1) Empty or all-space: reject
        if (in == null || in.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        // 2) Reject '=' because it is a UI action, not expression content
        if (in.indexOf('=') >= 0) {
            throw new IllegalArgumentException("Equals not allowed in expression text");
        }

        // 3) Remove whitespace and normalize visual operators
        String s = in.replaceAll("\\s+", "")
                     .replace('×', '*')
                     .replace('÷', '/');

        // 4) Fraction-style view: single top-level "a/b" becomes "(a)/(b)"
        int slash = s.indexOf('/');
        if (slash > 0 && slash == s.lastIndexOf('/')) {
            String left = s.substring(0, slash);
            String right = s.substring(slash + 1);
            if (!left.isEmpty() && !right.isEmpty() && isAtomic(left) && isAtomic(right)) {
                s = "(" + left + ")/(" + right + ")";
            }
        }

        // 5) Character whitelist
        if (!s.matches("[0-9+\\-*/().]*")) {
            throw new IllegalArgumentException("Invalid characters");
        }

        // 6) Invalid start (leading '-' is okay; others are not)
        if (s.startsWith("+") || s.startsWith("*") || s.startsWith("/")) {
            throw new IllegalArgumentException("Invalid start");
        }

        // 7) Invalid end (operator or dot cannot terminate the expression)
        if (s.endsWith("+") || s.endsWith("-") || s.endsWith("*") || s.endsWith("/") || s.endsWith(".")) {
            throw new IllegalArgumentException("Invalid end");
        }

        // 8) Reject multiple dots in any single numeric token
        for (String seg : s.split("[+\\-*/()]+")) {
            if (seg.indexOf('.') != seg.lastIndexOf('.')) {
                throw new IllegalArgumentException("Multiple dots in number");
            }
        }

        // 9) Reject operator immediately after a dot (e.g., "2.+3")
        if (s.contains(".+") || s.contains(".-") || s.contains(".*") || s.contains("./")) {
            throw new IllegalArgumentException("Operator after dot");
        }

        return s;
    }

    /** Treats a parenthesized or numeric token as a single atomic unit for the fraction view. */
    private static boolean isAtomic(String t) {
        if (t.isEmpty()) return false;
        if (t.matches("\\(-?[0-9.]+\\)")) return true;
        return t.matches("-?[0-9.]+");
    }
}
