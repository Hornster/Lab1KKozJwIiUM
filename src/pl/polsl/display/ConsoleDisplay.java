package pl.polsl.display;

public class ConsoleDisplay implements IDisplayModule
{
    @Override
    public void ShowData(String msg) {
        System.out.print(msg);
    }

    @Override
    public void ShowData(int number) {
        System.out.print(number);
    }

    @Override
    public void ShowData(double number) {
        System.out.print(number);
    }
}
