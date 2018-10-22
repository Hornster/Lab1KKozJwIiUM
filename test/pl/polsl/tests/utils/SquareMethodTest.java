package pl.polsl.tests.utils;

import pl.polsl.data.IntegralData;
import pl.polsl.utils.SquareMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SquareMethodTest {
    IntegralData integralData;
    SquareMethod squareMethod;
    @Before
    public void iniTests()
    {
        integralData = new IntegralData(0, 0.8);
        squareMethod = new SquareMethod(integralData, 100000);
    }

    @Test
    public void calculateIntegralTest()
    {
        double result = squareMethod.calculateIntegral();

        Assert.assertTrue(result < 0.9046 && result > 0.9045);
    }
}
