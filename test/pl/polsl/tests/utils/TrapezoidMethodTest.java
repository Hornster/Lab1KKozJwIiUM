package pl.polsl.tests.utils;

import pl.polsl.data.IntegralData;
import pl.polsl.exceptions.NoFunctionAssignedException;
import pl.polsl.utils.TrapezoidMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**Contains tests for the {@link pl.polsl.utils.TrapezoidMethod} class.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public class TrapezoidMethodTest {

    /**An example integral to test*/
    IntegralData integral;
    /**Tested method object.*/
    TrapezoidMethod method;
    /**Initializes objects required for tests.*/
    @Before
    public void initializeData()
    {
        integral= new IntegralData(0.0, 0.8);
        method =new TrapezoidMethod(integral, 1000000);
    }
    /**Tests calculateIntegral method. Will be passed if the returned by the method value
     * fits in given range.*/
    @Test
    public void testCalculateIntegral()
    {
        try {
            double result = method.calculateIntegral();
            Assert.assertTrue(result < 0.9046 && result > 0.9045);
        }
        catch(NoFunctionAssignedException ex)
        {
            fail("Failed test: " + ex.getMessage());
        }
    }


}
