package pl.polsl.model;

import pl.polsl.controller.local.CommandParser;

import java.util.LinkedList;
import java.util.List;
/** Stores information about parsed command (status, description, data).
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0
 */
public class ServerCommand {
    /**
     * Description associated with this command.
     */
    private String description;
    /**
     * Type of the command.
     */
    private CommandParser.commandType commandType;
    /**
     * List containing data from the command.
     */
    private List<String> data = new LinkedList<>();

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list with command data.
     * @return List containing the data associated with command. Every data chunk in form of a string.
     */
    public List<String> getData() {
        return data;
    }


    public CommandParser.commandType getCommandType() {
        return commandType;
    }

    public ServerCommand(CommandParser.commandType commandType)
    {
        this.commandType = commandType;
    }
    public void addValue(String value)
    {
        data.add(value);
    }
    public void addValues(List<String> values)
    {
        data = values;
    }


}
