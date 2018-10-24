package pl.polsl.data;
/**Container for data about an integral that shall be calculated.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public class IntegralData {
    /**The beginning x value of range.*/
    private double begin;
    /**The end x value of range.*/
    private double end;
    /**Calculates the value of integral in position equal to x. For now, the integral is sewn into the code.
     * @param x The x for which the integral value shall be calculated.
     * @return The y (value) of the integral in x.*/
    public double calcValue(double x)
    {
        return (x*x)/2+8*(x*x*x);
    }
    /**A definite integral has to have a range.
     * @param begin The beginning of the range.
     * @param end The end of the range.*/
    public IntegralData(double begin, double end)
    {
        this.begin = begin;
        this.end = end;
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
