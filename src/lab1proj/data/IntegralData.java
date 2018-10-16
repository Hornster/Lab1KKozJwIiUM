package lab1proj.data;

public class IntegralData {
    private double begin;
    private double end;
    public double calcValue(double x)
    {
        return (x*x)/2+8*(x*x*x);
    }

    public IntegralData(double begin, double end)
    {
        this.begin = begin;
        this.end = end;
    }

    public  double getBeginning()
    {
        return this.begin;
    }
    public double getEnd()
    {
        return this.end;
    }
    public void setBeginning(double beginValue)
    {
        begin = beginValue;
    }
    public void setEnd(double endValue)
    {
        end = endValue;
    }

}
