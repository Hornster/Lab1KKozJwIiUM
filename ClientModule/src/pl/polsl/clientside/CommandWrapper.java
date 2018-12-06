package pl.polsl.clientside;

/** Used to determine if received commands are correct.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0
 */
public class CommandWrapper {
    /**
     * Stores possible states of the commands. INCORRECT means the command is corrupted - needs to be returned to user.
     * Other indicate what type of command is this.
     */
    public enum commandType{DISCONNECT, HELP, SET_INTEGRAL, SET_METHOD, CALCULATE, GET_HISTORY, INCORRECT}
}

//TODO add implementation that creates a simple command in form of a string (type, then arguments).
//TODO add method(s) that search command types in the beginning of the answer from the server
