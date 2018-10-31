package pl.polsl.utils;

import org.mariuszgromada.math.mxparser.Expression;

/**Class responsible for checking if data is correct.*/
public class DataChk {
    /**Validates the syntax of passed function.
     * @param functionSyntax Function to validate.
     * @return True if syntax is valid. False otherwise.*/
    public static boolean validateFunctionSyntax(String functionSyntax)
    {
        Expression expression = new Expression (functionSyntax);

        if(!expression.checkSyntax()) {
            return false;
        }
        else{
            return true;
        }
    }
}
