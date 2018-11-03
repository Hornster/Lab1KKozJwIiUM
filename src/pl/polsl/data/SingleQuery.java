package pl.polsl.data;
/**Single memento that stores data about given query.
 * @author Karol Kozuch
 * @version 1.0*/
public class SingleQuery {
    /**Math function provided for the query.*/
    private String mathFunction;
    /**Argument of the function above.*/
    private Character argument;
    /**Beginning of the definite integral.*/
    private int rangeBegin;
    /**End of the definite integral.*/
    private int rangeEnd;
    /**Amount of objects used in approximation.*/
    private int accuracy;
    /**Selected approximation method.*/
    private char method;
    /**Result of the calculations.*/
    private double result;

    SingleQuery(SingleQuery source)
    {
        this.mathFunction = new String(source.mathFunction);
        this.argument = source.argument;
        this.rangeBegin = source.rangeBegin;
        this.rangeEnd = source.rangeEnd;
        this.accuracy = source.accuracy;
        this.method = source.method;
        this.result = source.result;
    }


    public String getMathFunction() {
        return mathFunction;
    }

    public void setMathFunction(String mathFunction) {
        this.mathFunction = mathFunction;
    }

    public Character getArgument() {
        return argument;
    }

    public void setArgument(Character argument) {
        this.argument = argument;
    }

    public int getRangeBegin() {
        return rangeBegin;
    }

    public void setRangeBegin(int rangeBegin) {
        this.rangeBegin = rangeBegin;
    }

    public int getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(int rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public char getMethod() {
        return method;
    }

    public void setMethod(char method) {
        this.method = method;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
