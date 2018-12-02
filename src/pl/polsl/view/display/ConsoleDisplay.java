package pl.polsl.view.display;

/**Responsible for showing data to the user through the console.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public class ConsoleDisplay implements IDisplayModule
{
    /**Prints a message in form of a string to the console.
     * @param msg A string of characters to print.*/
    @Override
    public void showData(String msg) {
        System.out.print(msg);
    }
    /**Prints a message in form of an integer to the console.
     * @param number An integer to print.*/
    @Override
    public void showData(int number) {
        System.out.print(number);
    }
    /**Prints a double precision floating point to the console.
     * @param number A double precision floating point to print.*/
    @Override
    public void showData(double number) {
        System.out.print(number);
    }
}
