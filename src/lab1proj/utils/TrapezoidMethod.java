package lab1proj.utils;

import com.sun.istack.internal.NotNull;
import lab1proj.data.IntegralData;
/**Used to calculate the value of given integral in certain range. Trapezoidal method.*/
public class TrapezoidMethod implements IntegralCalculator{
    /**Amount of trapezoids used to calculate the integral value.*/
    private int precisionLevel;
    private IntegralData integralData;

    public TrapezoidMethod(@NotNull IntegralData function)
    {
        this(function, 100);
    }
    public TrapezoidMethod(@NotNull IntegralData function, int precision)
    {
        integralData = function;
        precisionLevel = precision;
    }
    /**Calculates length of a single trapezoid along X axis.
     * @return Returns the length of a single trapezoid along X axis.*/
    private double findInterval()
    {
        return (integralData.getEnd()-integralData.getBeginning())/precisionLevel;
    }
    /**Calculates area of a single trapezoid.
     * @param currentPos The beginning X of the currently calculated trapezoid.
     * @param interval The height (on X axis) of the trapezoid.
     * @return Area of a trapezoid.*/
    private double calcTrapezoid(double currentPos, double interval)
    {
        double singleResult = 0.0;
        singleResult += integralData.calcValue(currentPos);
        currentPos += interval;
        singleResult += integralData.calcValue(currentPos);
        singleResult *= interval;
        singleResult /= (double)2;

        return singleResult;
    }
    /**Set a new amount of trapezoids that will be used to calculate value. More is slower, but more accurate.
     * @param precision New precision for the algorithm.*/
    @Override
    public void setPrecision(int precision) {
        precisionLevel = precision;
    }
    /**Sets new integral for the algorithm.
     * @param integral New integral.*/
    @Override
    public void setIntegralData(@NotNull IntegralData integral) {
        integralData = integral;
    }
    /**Begins the calculation.
     * @return Returns the estimated value of the specified integral in its specified range.*/
    @Override
    public double calculateIntegral() {
        double interval = findInterval();
        double currentPos = integralData.getBeginning();
        double overallResult = 0.0;

        do
        {
            overallResult += calcTrapezoid(currentPos, interval);
            currentPos += interval;

        }while (currentPos <= integralData.getEnd());

        return overallResult;
    }
}
