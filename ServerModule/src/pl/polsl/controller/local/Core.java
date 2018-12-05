package pl.polsl.controller.local;

import javafx.css.ParsedValue;
import javafx.util.Pair;
import pl.polsl.model.PredefinedCommunicates;
import pl.polsl.model.ServerCommand;
import pl.polsl.model.exceptions.IntegralCalculationException;
import pl.polsl.model.queryHistory.QueryHistory;
import pl.polsl.server.CommandParser;
import pl.polsl.server.ConfigLoader;
import pl.polsl.server.ConnectionManager;
import pl.polsl.model.queryHistory.QueryManager;

import java.io.IOException;

/**Contains the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.2*/
public class Core {
    /**Stores information about already processed approximation queries.*/
    private QueryManager queryManager;
    /**Responsible for sending messages to the user.*/
    private ConnectionManager connectionManager;
    /**Parses commands delivered through sockets.*/
    private CommandParser commandParser= new CommandParser();
    /**Manages calculation performing and data assigning (needed for the calculations).*/
    private CalcModuleServerAdapter calculationModule;
    /**States of the program.*/
    private enum programStates{EXIT, RETRIEVE_COMMAND, PROCESS_COMMAND}


    /**Stores current state of the program.*/
    private programStates state;
    /**Placeholder for the Main object.*/
    private static Core core;


    /**Ctor that initializes I/O modules and integral data (integral data for now - this will be changed in exercise 2).*/
    private Core()
    {
        state = programStates.RETRIEVE_COMMAND;
        calculationModule = new CalcModuleServerAdapter(new CalculationModule());
        queryManager = new QueryManager();

        calculationModule.addListener(queryManager);
    }
    /**Enables the process of calculating the integral value.*/
    private ServerCommand triggerCalculations(ServerCommand processedCommand)
    {
        processedCommand.setDescription(PredefinedCommunicates.genericAcknowledge());
        try {
            Double result = calculationModule.performCalculation();

            processedCommand.addValue(result.toString());
        }
        catch(IntegralCalculationException ex)
        {
            processedCommand.setDescription(ex.getMessage());
            processedCommand.setCommandType(CommandParser.commandType.INCORRECT);
        }

        return processedCommand;
    }
    /**Changes state of the program.
     * @param newState The new state of the program.*/
    private void changeState(programStates newState)
    {
        state = newState;
    }


    /**Manages selection of the calculation method process.*/
    private ServerCommand configureMethod(ServerCommand processedCommand)
    {
        processedCommand.setDescription(PredefinedCommunicates.genericAcknowledge());
        if(!calculationModule.selectMethod(processedCommand))
        {
            processedCommand.setCommandType(CommandParser.commandType.INCORRECT);
            processedCommand.setDescription(PredefinedCommunicates.incorrectCalcMethod());
        }
        else {
            calculationModule.setAccuracy(processedCommand);
        }

        return processedCommand;
    }

    /**
     * Manages the process of setting the range of the integral calculation.
     * @param command Command with data that will be assigned to integral.
     */
    private ServerCommand configureIntegral(ServerCommand command)
    {
        calculationModule.setFunction(command);
        calculationModule.assignNewIntegralRange(command);

        return command;
    }
    /**Manages process of showing the history to the user.*/
    private ServerCommand showCalculationHistory(ServerCommand processedCommand)
    {
        QueryHistory history = queryManager.getQueryHistory();
        if(history.size() <= 0)
        {
            processedCommand.addValue(PredefinedCommunicates.noQueriesInHistory());
        }
        else
        {
            for (Object q : history) {
                processedCommand.addValue(q.toString());
            }
        }

        return processedCommand;
    }

    /**
     * Sens passed message to client, right after conversion to String form.
     * @param command Command containing message to send.
     */
    private void sendMessage(ServerCommand command)
    {
        connectionManager.SendAnswer(command.toString());
    }
    /**
     * Manages client disconnection sequence.
     * @param command Command associated with disconnection.
     */
    private void DisconnectClient(ServerCommand command)
    {
        command.setDescription(PredefinedCommunicates.genericAcknowledge());
        sendMessage(command);
        try {
            connectionManager.close();
        }
        catch(IOException ex)
        {
            System.out.println("Unable to disconnect client! Socket corrupted!");
            changeState(programStates.EXIT);
        }
    }
    /**
     * Retrieves command from the connectionManager. Passes it to commandParser and returns parsed command.
     * @return Newly acquired and parsed command.
     */
    private ServerCommand retrieveCommand()
    {
        ServerCommand parsedCommand = null;
        String newCommand;
        try {
            newCommand = connectionManager.RetrieveCommand();
            parsedCommand = commandParser.ParseCommand(newCommand);
        }
        catch(IOException ex)
        {
            System.out.println("Unable to retrieve command! Error: " + ex.getMessage());
            try
            {
                connectionManager.close();
            }
            catch(IOException exc)
            {
                System.out.println("Unable to disconnect client, possible socket corruption! Shutting down server. Error: "+ exc.getMessage());
                changeState(programStates.EXIT);
            }

        }

        return parsedCommand;
    }

    /**
     * Manages processing of sent command.
     * @param processedCommand Command to process.
     */
    private void processCommand(ServerCommand processedCommand)
    {
        ServerCommand commandToReturn = null;
        switch(processedCommand.getCommandType())
        {
            case DISCONNECT:
                processedCommand.setDescription(PredefinedCommunicates.genericAcknowledge());
                DisconnectClient(processedCommand);
                return;                             //DISCONNECT is special - it has to sebd a farewell message first and THEN disconnect the client.
            case HELP:
            case INCORRECT:
                processedCommand.setDescription(PredefinedCommunicates.genericAcknowledge());
                break;                             //The command doesn't require too much of work - return answer instantly.
            case SET_INTEGRAL:
                processedCommand.setDescription(PredefinedCommunicates.valueAssigned());
                commandToReturn = configureIntegral(processedCommand);
                break;
            case SET_METHOD:
                processedCommand.setDescription(PredefinedCommunicates.valueAssigned());
                commandToReturn = configureMethod(processedCommand);
                break;
            case CALCULATE:
                processedCommand.setDescription(PredefinedCommunicates.calcResult());
                commandToReturn = triggerCalculations(processedCommand);
                break;
            case GET_HISTORY:
                processedCommand.setDescription(PredefinedCommunicates.genericAcknowledge());
                commandToReturn = showCalculationHistory(processedCommand);
                break;
        }

        sendMessage(commandToReturn);
    }
    /**Main loop of the program*/
    private void mainLoop()
    {
        ServerCommand processedCommand = null;
        connectionManager.printWelcome();

        while(state != programStates.EXIT)
        {
            switch(state) {
                case PROCESS_COMMAND:
                    processCommand(processedCommand);

                    changeState(programStates.RETRIEVE_COMMAND);
                    break;

                case RETRIEVE_COMMAND:
                    processedCommand = retrieveCommand();

                    changeState(programStates.PROCESS_COMMAND);
                    break;

                default:
                    changeState(programStates.RETRIEVE_COMMAND);    //No idea how did you get here, but get back to the menu this instant!
                    break;
            }
        }
    }

    /**
     * Sets up  the server.
     * @throws IOException If socket of the server cannot be assigned to given port or the properties file cannot be recreated.
     */
    private void startServer() throws IOException
    {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.LoadConfiguration();

        int port = Integer.parseInt(configLoader.getUsedPort());
        connectionManager = new ConnectionManager(port);
        connectionManager.startServer();
    }
    /**Creates instance of the core, then calls the main loop of the program.*/
    static public void start()
    {
        if (core == null) {
            core = new Core();
        }

        try {
            core.startServer();
        }
        catch(IOException ex)
        {
            System.out.println("Unable to start server:\n" + ex.getMessage());
            core.changeState(programStates.EXIT);
        }
        core.mainLoop();
    }
}
