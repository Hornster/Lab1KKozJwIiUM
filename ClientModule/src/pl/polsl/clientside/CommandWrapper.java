package pl.polsl.clientside;

import java.util.List;

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

    /**
     * Creates a command that's compatible with server-side command structure.
     * @param type Type of the command. Used to recognize by the server parser what command is it really and how many arguments it needs.
     * @param arguments Arguments of the command.
     * @return A complete command in form of a string, ready to be sent.
     */
    public String createCommand(commandType type, List<String> arguments)
    {
        StringBuilder commandBuilder = new StringBuilder();

        commandBuilder.append(type.toString());
        commandBuilder.append(' ');
        for(String arg : arguments)
        {
            commandBuilder.append(arg );
            commandBuilder.append(',');
        }

        return commandBuilder.toString();
    }
}

