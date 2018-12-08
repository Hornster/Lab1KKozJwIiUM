package pl.polsl.clientside;

import com.sun.istack.internal.Nullable;

import java.util.LinkedList;
import java.util.List;

/** Used to determine if received commands are correct.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0
 */
public class CommandWrapper {
    /**
     * Arguments and values for given command are stored here, in FIFO manner.
     */
    List<String> commandArgs = new LinkedList<>();
    /**
     * Stores possible states of the commands. INCORRECT means the command is corrupted - needs to be returned to user.
     * Other indicate what type of command is this.
     */
    public enum commandType{DISCONNECT, HELP, SET_INTEGRAL, SET_METHOD, CALCULATE, GET_HISTORY, INCORRECT}

    /**
     * Creates a command that's compatible with server-side command structure.
     * @param type Type of the command. Used to recognize by the server parser what command is it really and how many arguments it needs.
     * @return A complete command in form of a string, ready to be sent.
     */
    public String createCommand(commandType type)
    {
        StringBuilder commandBuilder = new StringBuilder();

        commandBuilder.append(type.toString());

        for (String arg : commandArgs) {
            commandBuilder.append(' ');
            commandBuilder.append(arg);
            commandBuilder.append(',');
        }

        return commandBuilder.toString();
    }

    /**
     * Checks if provided answer from the server states that data sent by client was correct.
     * @param serverAnswer String containing answer from the server.
     * @return TRUE if the command send by the client was acknowledged. FALSE if the command was incorrect.
     */
    public boolean ChkIfAnswerCorrect(String serverAnswer)
    {
        serverAnswer = serverAnswer.toLowerCase();
        return !serverAnswer.startsWith(commandType.INCORRECT.toString().toLowerCase());
    }

    /**
     * Clears the arguments list.
     */
    public void newArgList()
    {
        commandArgs.clear();
    }

    /**
     * Adds argument to the list, in FIFO manner.
     * @param arg New argument for the command.
     */
    public void addArgument(String arg)
    {
        commandArgs.add(arg);
    }
}

