package com.example.mycalculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;

/**
 * Utility class for evaluating mathematical expressions
 */
public class ExpressionEvaluator {
    
    /**
     * Evaluates a mathematical expression and returns the result as a string
     * 
     * @param expression The mathematical expression to evaluate
     * @return The result of the evaluation as a string
     * @throws Exception If the expression is invalid or cannot be evaluated
     */
    public static String evaluate(String expression) throws Exception {
        String processedExpression = expression
                .replace("×", "*")
                .replace("÷", "/");
        
        Expression expr = new ExpressionBuilder(processedExpression).build();
        Double result = expr.evaluate();

        // Check if the result is effectively an integer
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            return String.valueOf(result.longValue());
        } else {
            BigDecimal decimal = new BigDecimal(result);
            decimal = decimal.setScale(10, BigDecimal.ROUND_HALF_UP);

            return decimal.stripTrailingZeros().toPlainString();
        }
    }
}
