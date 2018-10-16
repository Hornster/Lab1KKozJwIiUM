package lab1proj.utils;


import lab1proj.data.IntegralData;

public interface IntegralCalculator {
    void setPrecision(int precision);
    void setIntegralData(IntegralData func);

    /**Calculates the integral value.
     * @return Returnscalculated integral value.*/
    double calculateIntegral();


}
