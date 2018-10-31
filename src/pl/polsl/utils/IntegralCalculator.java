package pl.polsl.utils;


import pl.polsl.data.IntegralData;
import pl.polsl.exceptions.NoFunctionAssignedException;

/**Interface for integral calculating methods.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public interface IntegralCalculator {
    /**Sets the precision (in form of amount of geometric shapes used in calculations, for example) of
     * the result.
     * @param precision The desired precision. Shall be positive unless used algorithm states differently.*/
    void setPrecision(int precision);
    /**Sets new integral to be calculated by the algorithm.
     * @param integralData Object containing data of the new integral.*/
    void setIntegralData(IntegralData integralData);

    /**Calculates the integral value.
     * @return Calculated integral value.*/
    double calculateIntegral() throws NoFunctionAssignedException;
}
