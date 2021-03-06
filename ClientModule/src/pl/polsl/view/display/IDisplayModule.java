package pl.polsl.view.display;
/**Interface with all necessary methods that a display module shall contain.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public interface IDisplayModule
{
    /**Shows the message to the user.
     * @param msg A String to show.*/
    void showData(String msg);
    /**Shows a number to the user.
     * @param number An integer to show. */
    void showData(int number);
    /**Shows a double precision floating point to the user.
     * @param number A double to show. */
    void showData(double number);
}
