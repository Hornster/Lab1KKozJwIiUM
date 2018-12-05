package pl.polsl.controller.local;

import com.sun.security.ntlm.Server;
import pl.polsl.model.CommandsDescriptions;
import pl.polsl.model.IntegralData;
import pl.polsl.model.ServerCommand;
import pl.polsl.utility.dataCheck.DataChk;
import pl.polsl.utility.dataCheck.ParseModifyString;

import java.util.LinkedList;
import java.util.List;

/** Used to determine if received commands are correct.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0
 */
public class CommandParser {
    /**
     * Stores possible states of the commands. INCORRECT means the command is corrupted - needs to be returned to user.
     * Other indicate what type of command is this.
     */
    public enum commandType{DISCONNECT, HELP, SET_INTEGRAL, SET_METHOD, CALCULATE, GET_HISTORY, INCORRECT}
    CommandsDescriptions commandsDescriptions = new CommandsDescriptions();
    /**
     * Stores parsers that can be used to find out info about the command.
     */
    List<IParser> parsers = new LinkedList<>();

    public CommandParser()
    {
        parsers.add(helpCommandParser);
        parsers.add(disconnectCommandParser);
        parsers.add(setIntegralCommandParser);
        parsers.add(calculateCommandParser);
        parsers.add(getHistoryCommandParser);
        parsers.add(setMethodCommandParser);
    }

    /**
     * Reads data chunks from the command string.
     * @param type Type of command.
     * @param inputString The command itself.
     * @return List of available data chunks in the command.
     */
    private List<String> getDataStrings(commandType type, String inputString)
    {
        List<String> dataStrings = new LinkedList<>();
        String singleDataChunk = null;                         //Will be used in loop to find single arguments.
        int nextDataBeginIndex = type.toString().length();
        String data = inputString.substring(nextDataBeginIndex);

        nextDataBeginIndex = data.indexOf(',');

        while(nextDataBeginIndex >= 0)                   //Data chunks are separated by commas, indexOf returns -1 upon not finding next element
        {
            singleDataChunk = data.substring(0, nextDataBeginIndex);     //Save next data chunk
            data = data.substring(nextDataBeginIndex+1);                     //Cut the read data chunk together with the comma (+1)
            dataStrings.add(singleDataChunk);
            nextDataBeginIndex = data.indexOf(',');
        }

        if(!data.isEmpty()) {
            dataStrings.add(data);
        }


        return dataStrings;
    }
    /**
     * Checks if the received command is of HELP type.
     */
    private IParser helpCommandParser = new IParser()
    {
        /**
         * Checks if the input contains HELP command.
         * @param input Command to parse.
         * @return ServerCommand containing descriptions of all client-available commands in values field,together with a header.
         */
        @Override
        public ServerCommand parseCommand(String input)
        {
            if(!input.startsWith(commandType.HELP.toString().toLowerCase()))
            {
                return null;
            }

            ServerCommand command = new ServerCommand(commandType.HELP);
            command.setDescription("Contains client-available commands descriptions.");
            commandType[] commandTypes = commandType.values();
            command.addValue("Available commands: \n");

            for(int i = 0; i < commandType.values().length; i++)
            {
                command.addValue(commandsDescriptions.getCommandDesc(commandTypes[i]));
            }

            return command;
        }
    };
    /**
     * Checks if the received command is of DISCONNECT type.
     */
    private IParser disconnectCommandParser = new IParser()
    {
        /**
         * Checks if the input has a disconnect command.
         * @param input Input from socket.
         * @return ServerCommand with DISCONNECT state. NULL if not recognized as DISCONNECT command.
         */
        @Override
        public ServerCommand parseCommand(String input) {
            if(!(input.startsWith(commandType.DISCONNECT.toString().toLowerCase()) ||
                    input.startsWith("quit")))
            {
                return null;
            }

            ServerCommand command = new ServerCommand(commandType.DISCONNECT);
            command.setDescription("Client disconnects.");
            command.addValue("Goodbye! Be well!");

            return command;
        }
    };
    /**
     * Checks if the received command is of SET_INTEGRAL type.
     */
    private IParser setIntegralCommandParser = new IParser()
    {
        /**
         * Tries to parse the command to suit the SET_INTEGRAL command needs.
         * @param input Command received from the user.
         * @return ServerCommand with data in values field. Null if not recognized. ServerCommand with error description in the description field if cannot be parsed.
         */
        @Override
        public ServerCommand parseCommand(String input) {
            if(!input.startsWith(commandType.SET_INTEGRAL.toString().toLowerCase()))      //Check if the command begins with proper designator.
            {
                return null;
            }

            List<String> dataChunks = getDataStrings(commandType.SET_INTEGRAL, input);

            if(dataChunks.size() < IntegralData.neededValues)               //Check if the command has the 3 required values.
            {
                return commandsDescriptions.createIncorrectCommand("Error - too little arguments. For " +
                        commandType.SET_INTEGRAL.toString() + " command, a formula, begin and end of range are needed. \n" +
                        "Send command \"help\" for details and example.");
            }

            if(!DataChk.validateFunctionSyntax(dataChunks.get(0)))          //Check if first argument is the formula for the integral.
            {
                return commandsDescriptions.createIncorrectCommand("Error - incorrect formula syntax.");
            }

            for(int i = 1; i < IntegralData.neededValues; i++)              //Check if the second and third argument define the range (as decimals).
            {
                if(!ParseModifyString.tryStringToDouble(dataChunks.get(i)))
                {
                    return commandsDescriptions.createIncorrectCommand("Error - one of the range arguments was not a decimal number. Remember to use dot instead of coma.");
                }
            }

            ServerCommand parsedCommand = new ServerCommand(commandType.SET_INTEGRAL);      //If no errors found - Create proper command and return it.
            parsedCommand.addValues(dataChunks);

            return parsedCommand;
        }
    };
    /**
     * Checks if the received command is of SET_METHOD type.
     */
    private IParser setMethodCommandParser = new IParser() {
        /**
         * Tries to parse passed command as a SET_METHOD command.
         * @param input Command provided by the client.
         * @return ServerCommand containing data for approximation method. ServerCommand of INCORRECT type if not managed to parse correctly. NULL if not recognized as SET_METHOD command.
         */
        @Override
        public ServerCommand parseCommand(String input) {
            if(!input.startsWith(commandType.SET_METHOD.toString().toLowerCase()))
            {
                return null;
            }

            List<String> dataChunks = getDataStrings(commandType.SET_METHOD, input);

            if(dataChunks.size() < 2)
            {
                return commandsDescriptions.createIncorrectCommand("Error - too little arguments in command call. " +
                        "The command requires 2 arguments: <method>, <shapes_count>. Input \"help\" command for details.");
            }
            if(!ParseModifyString.tryStringToInt(dataChunks.get(1)))
            {
                return commandsDescriptions.createIncorrectCommand("Error - second argument must be positive integer!");
            }
            else if(Integer.parseInt(dataChunks.get(1)) <=0 )
            {
                return commandsDescriptions.createIncorrectCommand("Error - second argument must be greater than 0!");
            }

            ServerCommand parsedCommand = new ServerCommand(commandType.SET_METHOD);
            parsedCommand.addValues(dataChunks);
            parsedCommand.setDescription("Values for setting the approximation method.");

            return parsedCommand;
        }
    };
    /**
     * Checks if the received command is of CALCULATE type.
     */
    private IParser calculateCommandParser = new IParser()
    {
        /**
         * Checks if the command is of CALCULATE type.
         * @param input Command provided by client.
         * @return ServerCommand of CALCULATE type. NULL if not recognized as CALCULATE command.
         */
        @Override
        public ServerCommand parseCommand(String input) {
            if(!input.startsWith(commandType.CALCULATE.toString().toLowerCase()))
            {
                return null;
            }

            ServerCommand parsedCommand = new ServerCommand(commandType.CALCULATE);
            parsedCommand.setDescription("Request for approximation result.");

            return parsedCommand;
        }
    };
    /**
     * Checks if the received command is of GET_HISTORY type.
     */
    private IParser getHistoryCommandParser = new IParser() {
        /**
         * Tries to parse the command to GET_HISTORY type.
         * @param input Command delivered by client.
         * @return ServerCommand of GET_HISTORY type. NULL if not recognized as GET_HISTORY.
         */
        @Override
        public ServerCommand parseCommand(String input) {
            if(!input.startsWith(commandType.GET_HISTORY.toString().toLowerCase()))
            {
                return null;
            }

            ServerCommand command = new ServerCommand(commandType.GET_HISTORY);
            command.setDescription("Request for the query history.");

            return command;
        }
    };


    /**
     * Manages command parsing process.
     * @param command Command to parse.
     * @return Object with command data.
     */
    public ServerCommand ParseCommand(String command)
    {
        command = command.toLowerCase();
        command = ParseModifyString.removeWhiteChars(command);
        ServerCommand resultCommand = null;

        for(IParser parser : parsers)
        {
            resultCommand = parser.parseCommand(command);

            if(resultCommand != null) {
                break;
            }
        }

        if(resultCommand == null)
        {
            resultCommand = new ServerCommand(commandType.INCORRECT);
            resultCommand.setDescription("Command not recognized by the server.");
            resultCommand.addValue("Unknown command. Type \"" + commandType.HELP.toString() + "\" in order to receive" +
                    "list of available commands.");
        }

        return resultCommand;
    }

    /**
     * Used to perform command parsing.
     */
    interface IParser
    {
        ServerCommand parseCommand(String input);
    }

}
