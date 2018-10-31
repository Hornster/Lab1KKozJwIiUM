package pl.polsl.data;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import pl.polsl.exceptions.NoFunctionAssignedException;

/**Container for data about an integral that shall be calculated.
 * Please keep in mind that the function shall be initialized before calling calcValue()
 * and that it shall be changed whether is it correct.
 * Stuff to initialize:
 * -range (constructor)
 * -argument (by default - 'x')
 * -integral function.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.2*/
public class IntegralData {
    /**The expression used in calculations.*/
    Expression exp1;
    /**The argument of the integral.*/
    char argument = 'x';
    /**The function used for calculations.*/
    private Function integralFunc = null;
    /**The beginning x value of range.*/
    private double begin;
    /**The end x value of range.*/
    private double end;
    /**Calculates the value of integral in position equal to x. For now, the integral is sewn into the code.
     * @throws NoFunctionAssignedException - when the integral function was not set before calculations.
     * @param x The x for which the integral value shall be calculated.
     * @return The y (value) of the integral in x.*/
    public double calcValue(double x) throws NoFunctionAssignedException
    {
        if(integralFunc == null)
            throw new NoFunctionAssignedException("Call for a function that was not initialized first!");

        //Expression e1 = new Expression("f(" + x + ")", integralFunc);
        exp1.setArgumentValue("x", x);
        return exp1.calculate();
    }
    /**A definite integral has to have a range.
     * @param begin The beginning of the range.
     * @param end The end of the range.*/
    public IntegralData(double begin, double end)
    {
        this.begin = begin;
        this.end = end;
    }

    /**Sets the integral function to calculate.
     * @param newFunction New integral to calculate.
     * @param arg The argument.*/
    public void setIntegralFunc(String newFunction, char arg)
    {
        argument = arg;
        integralFunc = new Function("f(" + argument + ")=" + newFunction);
        exp1 = new Expression("f(" + argument + ")",integralFunc);
        exp1.addArguments(new Argument("x", 0.0));
    }
    /**Sets the integral function to calculate. Uses default/already modified argument.
     * @param newFunction New integral to calculate.*/
    public void setIntegralFunc(String newFunction)
    {
        this.setIntegralFunc(newFunction, argument);
    }
    /**Getter for the beginning of the integral range.
     * @return The beginning of the integral range.*/
    public  double getBeginning()
    {
        return this.begin;
    }
    /**Getter for the end of the integral range.
     * @return The end of the integral range.*/
    public double getEnd()
    {
        return this.end;
    }
}
