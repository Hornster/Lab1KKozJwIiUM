package pl.polsl.utils;

import com.sun.istack.internal.NotNull;
import pl.polsl.data.IntegralData;
import pl.polsl.exceptions.NoFunctionAssignedException;

/**Calculates value of an integral in given range using squares method.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.2*/
public class SquareMethod implements IntegralCalculator {
    /**The amount of squares used to approximate the value.*/
    int precisionLevel;
    /**Currently used integral.*/
    IntegralData usedIntegralData;

    public SquareMethod(@NotNull IntegralData function){ this(function, 100);}
    public SquareMethod(@NotNull IntegralData function, int precisionLevel)
    {
        usedIntegralData = function;
        this.precisionLevel = precisionLevel;
    }
    /**Sets new precision (amount of squares used for calculation).
     * Higher is better, but slower.*/
    @Override
    public void setPrecision(int precision) {
        precisionLevel = precision;
    }
    /**Sets the integral data.
     * @param integralData New integral data.*/
    @Override
    public void setIntegralData(IntegralData integralData) {
        usedIntegralData = integralData;
    }
    /**Calculates single rectangle's area.
     * @param currentBeginning X position of the rectangle on the axis.
     * @param interval The width along X axis of the rectangle.
     * @return Area of the rectangle used in approximation.*/
    private double calcSingleRectangle(double currentBeginning, double interval) throws NoFunctionAssignedException
    {
        double result = 0.0;
        result = usedIntegralData.calcValue(currentBeginning);
        result *= interval;

        return result;
    }
    /**Calculates the stored integral.
     * @return Value of the integral in specified earlier range.*/
    @Override
    public double calculateIntegral() throws NoFunctionAssignedException {
        double overallResult = 0.0;
        double currentPos = usedIntegralData.getBeginning();
        double interval = calcInterval();

        while(currentPos < usedIntegralData.getEnd())
        {

            overallResult+=Math.abs(calcSingleRectangle(currentPos, interval));
            currentPos += interval;
        }

        return overallResult;
    }
    /**Calculates the width of a single rectangle basing on the @precisionLevel and @usedIntegralData beginning and end values.
     * @return Returns the width of a single rectangle (on X axis).*/
    private double calcInterval() {
        return (usedIntegralData.getEnd()- usedIntegralData.getBeginning())/precisionLevel;

    }
}
