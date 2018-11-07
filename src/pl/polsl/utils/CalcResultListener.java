package pl.polsl.utils;

import pl.polsl.data.CalculationData;
import pl.polsl.data.IntegralData;

/**Classes that want to react to new calculation need to implement this interface.
  * @author Karol KozuchGroup 4 Section 8
  * @version 1.0*/
public interface CalcResultListener {
    /**
     * Will be called by observed object.
     * @param calculationData Calculation data used in approximation.
     * @param integralData Integral and its data used in approximation.
     */
    void newCalculationPerformed(CalculationData calculationData, IntegralData integralData);
}
