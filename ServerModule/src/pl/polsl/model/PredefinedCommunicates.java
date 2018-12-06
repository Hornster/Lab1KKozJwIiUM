package pl.polsl.model;
/**Stores predefined communicates that the server can send to the clientside.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public class PredefinedCommunicates {
    /**
     * Called when there are no queries in the query history.
     * @return Message explaining absence of queries in the history.
     */
    public static  String noQueriesInHistory()
    {
        return "No queries recorded yet, Darling! \\n";
    }

    /**
     * Predefined message for answer to disconnect command.
     * @return A farewell message to disconnecting clientside.
     */
    public static String disconnectMessage()
    {
        return "Goodbye! Be well!";
    }

    /**
     * Predefined message informing about correct value assignment.
     * @return Confirmation of value/s assignment.
     */
    public static String valueAssigned()
    {
        return "Passed value\\s were correctly assigned.";
    }

    /**
     * Informs clientside about not recognized approximation method.
     * @return Error message indicating wrong input of approximation method that should be used.
     */
    public static String incorrectCalcMethod()
    {
        return "Provided method was not recognized.";
    }

    /**
     * Simple universal response to correctly parsed and processed command.
     * @return Simple acknowledgment.
     */
    public static String genericAcknowledge()
    {
        return "Acknowledged.\n";
    }

    /**
     * Response used when returning computed values.
     * @return Header for returned values.
     */
    public static String calcResult()
    {
        return "Calculations result: ";
    }

    /**
     * Header for HELP command.
     * @return Explanation of the HELP command.
     */
    public static String helpHeader()
    {
        return "Contains clientside-available commands descriptions.";
    }

    /**
     * Header for HELP command description.
     * @return Simple header for the commands description, informing about beginning of the description.
     */
    public static String helpDescriptionHeader()
    {
        return "Available commands: \n";
    }


}
