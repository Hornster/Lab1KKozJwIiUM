package pl.polsl.tests.utils;

import pl.polsl.data.IntegralData;
import pl.polsl.utils.SquareMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**Contains tests for the {@link pl.polsl.utils.SquareMethod} class.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public class SquareMethodTest {
    /**An example integral to test*/
    private IntegralData integralData;
    /**Tested method object.*/
    private SquareMethod squareMethod;
    /**Initializes objects required for tests.*/
    @Before
    public void iniTests()
    {
        integralData = new IntegralData(0, 0.8);
        squareMethod = new SquareMethod(integralData, 100000);
    }
    /**Tests calculateIntegral method. Will be passed if the returned by the method value
     * fits in given range.*/
    @Test
    public void calculateIntegralTest()
    {
        double result = squareMethod.calculateIntegral();

        Assert.assertTrue(result < 0.9046 && result > 0.9045);
    }
}
