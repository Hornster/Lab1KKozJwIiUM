package pl.polsl.data;
/**Single memento that stores data about given query.
 * @author Karol Kozuch
 * @version 1.1*/
public class SingleQuery {
    /**Math function provided for the query.*/
    private String mathFunction;
    /**Argument of the function above.*/
    private Character argument;
    /**Beginning of the definite integral.*/
    private double rangeBegin;
    /**End of the definite integral.*/
    private double rangeEnd;
    /**Amount of objects used in approximation.*/
    private int accuracy;
    /**Selected approximation method.*/
    private char method;
    /**Result of the calculation.*/
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

    public SingleQuery(IntegralData integralData, CalculationData calcData)
    {
        mathFunction = new String(integralData.getFunction());
        argument = integralData.getArgument();
        rangeBegin = integralData.getBeginning();
        rangeEnd = integralData.getEnd();
        accuracy = calcData.getAccuracy();
        method = calcData.getCalculationMethod();
        result = calcData.getResult();
    }

    public String getMathFunction() {
        return mathFunction;
    }
    /**Sets the integral function expression.
     * @param mathFunction New function expression.*/
    public void setMathFunction(String mathFunction) {
        this.mathFunction = mathFunction;
    }

    public Character getArgument() {
        return argument;
    }
    /**Sets the argument that is present in the function.
     * @param argument New integral function argument.*/
    public void setArgument(Character argument) {
        this.argument = argument;
    }

    public double getRangeBegin() {
        return rangeBegin;
    }
    /**Sets the beginning of the calculation range.
     * @param rangeBegin The new beginning of the calculation range.*/
    public void setRangeBegin(double rangeBegin) {
        this.rangeBegin = rangeBegin;
    }

    public double getRangeEnd() {
        return rangeEnd;
    }
    /**Sets the end of the calculation range.
     * @param rangeEnd The new end of the calculation range.*/
    public void setRangeEnd(double rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public int getAccuracy() {
        return accuracy;
    }
    /**Sets the amount of geometrical shapes that took part in approximation.
     * @param accuracy New amount of geometrical shapes used to calculate value.*/
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public char getMethod() {
        return method;
    }
    /**Sets the method used for approximation.
     * @param method Used method. t for trapezoidal, s for squares.*/
    public void setMethod(char method) {
        this.method = method;
    }

    public double getResult() {
        return result;
    }
    /**Sets the result of the approximation.
     * @param result The value calculated in the approximation process.*/
    public void setResult(double result) {
        this.result = result;
    }
    /**Creates easy to read report of integral calculation process.
     * @return Full description of stored integral calculation process.*/
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Query data: \n ");
        builder.append("Function: ");
        builder.append(mathFunction);
        builder.append("\n Of argument: ");
        builder.append(argument);
        builder.append("\n Calculation range: From ");
        builder.append(rangeBegin);
        builder.append(" to ");
        builder.append(rangeEnd);
        builder.append("\n Used geometrical shapes in approximation (accuracy): ");
        builder.append(accuracy);
        builder.append("\n Approximation method: ");
        builder.append(method);
        builder.append("\n Approximation result: ");
        builder.append(result);

        return builder.toString();
    }


}
