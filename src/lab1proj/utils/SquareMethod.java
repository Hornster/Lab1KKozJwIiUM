package lab1proj.utils;

import com.sun.istack.internal.NotNull;
import lab1proj.data.IntegralData;

public class SquareMethod implements IntegralCalculator {
    int precisionLevel;
    IntegralData usedMathFunction;

    public SquareMethod(@NotNull IntegralData function){ this(function, 100);}
    public SquareMethod(@NotNull IntegralData function, int precisionLevel)
    {
        usedMathFunction = function;
        this.precisionLevel = precisionLevel;
    }

    @Override
    public void setPrecision(int precision) {
        precisionLevel = precision;
    }

    @Override
    public void setIntegralData(IntegralData func) {
        usedMathFunction = func;
    }
    /**Calculates single rectangle's area.
     * @param currentBeginning X position of the rectangle on the axis.
     * @param interval The width along X axis of the rectangle.
     * @return Area of the rectangle used in approximation.*/
    private double calcSingleRectangle(double currentBeginning, double interval)
    {
        double result = 0.0;
        result = usedMathFunction.calcValue(currentBeginning);
        result *= interval;

        return result;
    }

    @Override
    public double calculateIntegral() {
        double overallResult = 0.0;
        double currentPos = usedMathFunction.getBeginning();
        double interval = calcInterval();
        while(currentPos < usedMathFunction.getEnd())
        {
            overallResult+=calcSingleRectangle(currentPos, interval);
            currentPos += interval;
        }

        return overallResult;
    }
    /**Calculates the width of a single rectangle basing on the @precisionLevel and @usedMathFunction beginning and end values.
     * @return Returns the width of a single rectangle (on X axis).*/
    private double calcInterval() {
        return (usedMathFunction.getEnd()-usedMathFunction.getBeginning())/precisionLevel;

    }
}
