package lab1proj.display;

public interface IDisplayModule
{
    /**Shows the message to the user.
     * @param msg A String to show.*/
    void ShowData(String msg);
    /**Shows the message to the user.
     * @param number An integer to show. */
    void ShowData(int number);
    /**Shows the message to the user.
     * @param number A double to show. */
    void ShowData(double number);
}
