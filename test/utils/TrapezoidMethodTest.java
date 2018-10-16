package utils;

import lab1proj.data.IntegralData;
import lab1proj.utils.TrapezoidMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TrapezoidMethodTest {
    IntegralData integral;
    TrapezoidMethod method;

    @Before
    public void initializeData()
    {
        integral= new IntegralData(0.0, 0.8);
        method =new TrapezoidMethod(integral, 1000000);
    }

    @Test
    public void calculateIntegralTest()
    {
        double result= method.calculateIntegral();
        Assert.assertTrue(result < 0.9046 && result > 0.9045);
    }


}
