package pl.polsl.service;

import javafx.util.Pair;
import pl.polsl.data.CalculationData;
import pl.polsl.data.IntegralData;
import pl.polsl.exceptions.NoFunctionAssignedException;
import pl.polsl.utils.IntegralCalculator;
import pl.polsl.utils.SquareMethod;
import pl.polsl.utils.TrapezoidMethod;

/** Manages data containers required for calculations and triggers the process itself.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0
 */
public class CalculationModule {
    /**The last decision (about used integral calc. method) made by the user*/
    private char lastMethodDecision = '\0';
    /**The integral to calculate.*/
    private IntegralData integral = new IntegralData(0.0, 1.0); //Default range
    /**Container for other data, like used method, result of approximation.*/
    private CalculationData calculationData = new CalculationData();
    /**Field for the calculation method.*/
    private IntegralCalculator calculator;

    /**Assigns input function to integral.
     * @param functionSyntax Syntax of newly input function.*/
    public void setFunction(String functionSyntax)
    {
        integral.setIntegralFunc(functionSyntax);
    }

    /**
     * Triggers the calculations and returns the result.
     * @return
     * @throws NoFunctionAssignedException Thrown when there was no function assigned or it was corrupt.
     */
    public double performCalculation() throws NoFunctionAssignedException {
        double result = calculator.calculateIntegral();
        calculationData.setResult(result);

        return result;
    }


    /**Responsible for selecting method of calculating the integral.
     * @param methodCode Char code of the method. 't' stands for Trapezoidal, 's' for Square method.
     * @return TRUE if the method has been set. FALSE otherwise.*/
    public boolean selectMethod(char methodCode)
    {

        if(lastMethodDecision == methodCode)
            return true; //No need to change algorithm since user selected the previously chosen.
        switch (methodCode)
        {
            case 't':
                calculator = new TrapezoidMethod(integral);
                lastMethodDecision = methodCode;
                return true;
            case 's':
                calculator = new SquareMethod(integral);
                lastMethodDecision = methodCode;
                return true;
            default:
                return false;
        }
    }

    /**
     * Assigns new range to the integral.
     * @param newRange Pair of doubles defining new range. First is beginning, second - end.
     */
    public void AssignNewIntegralRange(Pair<Double, Double> newRange)
    {
        integral.setBeginning(newRange.getKey());
        integral.setEnd(newRange.getValue());
    }

    /**
     * Assigns new accuracy (amount of geometrical shapes used in approximation).
     * @param newAccuracy How many geometrical shapes to use?(Positive number. Bigger is better, but slower).
     */
    public void setAccuracy(int newAccuracy)
    {
        calculationData.setAccuracy(newAccuracy);
    }
}
