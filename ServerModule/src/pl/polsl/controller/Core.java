package pl.polsl.controller;

import pl.polsl.controller.calculation.CalcModuleServerAdapter;
import pl.polsl.controller.calculation.CalculationModule;
import pl.polsl.model.CommandsDescriptions;
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
    private enum programStates{EXIT, RETRIEVE_COMMAND, PROCESS_COMMAND, AWAIT_CONNECTION}


    /**Stores current state of the program.*/
    private programStates state;
    /**Placeholder for the Main object.*/
    private static Core core;


    /**Ctor that initializes I/O modules and integral data (integral data for now - this will be changed in exercise 2).*/
    private Core()
    {
        state = programStates.AWAIT_CONNECTION;
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
     * Prepares the contents of HELP type command answer.
     * @param processedCommand Command that will be used to return the answer.
     * @return Command with every clientside-available command description.
     */
    private ServerCommand setHelp(ServerCommand processedCommand)
    {
        CommandsDescriptions commandsDescriptions= CommandsDescriptions.getInstance();
        String singleHelpLine;
        processedCommand.setDescription(PredefinedCommunicates.helpHeader());
        CommandParser.commandType[] commandTypes = CommandParser.commandType.values();
        processedCommand.addValue(PredefinedCommunicates.helpDescriptionHeader());

        for(int i = 0; i < CommandParser.commandType.values().length; i++)
        {
            CommandParser.commandType commandType = CommandParser.commandType.values()[i];
            if(commandParser.isCommandServerSideOnly(commandType)) {
                continue;                                           //Client doesn't need to know about server-side only commands.
            }

            singleHelpLine = commandType.toString();
            singleHelpLine = singleHelpLine.concat(' '+ commandsDescriptions.getCommandsDesc(commandType)); //Space bar for readability.
            processedCommand.addValue(singleHelpLine);
        }

        return processedCommand;
    }
    /**
     * Sens passed message to clientside, right after conversion to String form.
     * @param command Command containing message to send.
     */
    private void sendMessage(ServerCommand command)
    {
        connectionManager.SendAnswer(command.toString());
    }
    /**
     * Manages clientside disconnection sequence.
     * @param command Command associated with disconnection.
     */
    private void disconnectClient(ServerCommand command)
    {
        command.setDescription(PredefinedCommunicates.genericAcknowledge());
        sendMessage(command);
        try {
            connectionManager.close();
            changeState(programStates.AWAIT_CONNECTION);
        }
        catch(IOException ex)
        {
            System.out.println("Unable to disconnect clientside! Socket corrupted!");
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

            changeState(programStates.PROCESS_COMMAND);
        }
        catch(IOException ex)
        {
            System.out.println("Unable to retrieve command! Error: " + ex.getMessage());
            changeState(programStates.AWAIT_CONNECTION);                                    //Client is lost, so let's wait for new one
            try
            {
                connectionManager.close();
            }
            catch(IOException exc)
            {
                System.out.println("Unable to disconnect clientside, possible socket corruption! Shutting down server. Error: "+ exc.getMessage());
                changeState(programStates.EXIT);
            }

        }

        return parsedCommand;
    }

    /**
     * Calls methods responsible for awaiting for new connection.
     */
    private void awaitNewConnection()
    {
        try
        {
            connectionManager.awaitConnection();
        }
        catch(IOException ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
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
                disconnectClient(processedCommand);
                return;                             //DISCONNECT is special - it has to send a farewell message first and THEN disconnect the clientside.
            case HELP:
                commandToReturn = setHelp(processedCommand);
                break;
            case INCORRECT:
                commandToReturn = processedCommand;
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

                    if(processedCommand.getCommandType() != CommandParser.commandType.DISCONNECT) {
                        changeState(programStates.RETRIEVE_COMMAND);            //If clientside did not disconnect, we can keep listening for messages.
                    }                                                           //Otherwise simply let it switch to AWAIT_CONNECTION state.
                    break;

                case RETRIEVE_COMMAND:
                    processedCommand = retrieveCommand();
                    break;

                case AWAIT_CONNECTION:
                    awaitNewConnection();
                    changeState(programStates.RETRIEVE_COMMAND);
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
        configLoader.loadConfiguration();

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
