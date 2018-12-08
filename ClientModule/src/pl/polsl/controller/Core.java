package pl.polsl.controller;

import javafx.util.Pair;
import pl.polsl.clientside.CommandWrapper;
import pl.polsl.clientside.ConfigLoader;
import pl.polsl.clientside.ConnectionManager;
import pl.polsl.clientside.IConnectionManager;
import pl.polsl.view.display.ConsoleDisplay;
import pl.polsl.view.display.IDisplayModule;
import pl.polsl.view.input.ConsoleInput;
import pl.polsl.view.input.IInputModule;
import pl.polsl.view.userPrompt.ShowMsgToUser;

import java.io.IOException;

/**Contains the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.2*/
public class Core {
    CommandWrapper commandWrapper = new CommandWrapper();
    /**Manages connection with the server (establishes it, reads and sends data through streams, etc.)*/
    private IConnectionManager connectionManager = new ConnectionManager();
    /**Responsible for sending messages to the user.*/
    private ShowMsgToUser showMsgToUser;
    /**States of the program.*/
    private enum programStates{EXIT, CALCULATE_INTEGER, DISPLAY_HISTORY, MENU}

    /**Int value for the EXIT program state.*/
    private final int EXIT_STATE_VALUE = 2;
    /**Int value for DISPLAY_HISTORY program state.*/
    private final int SHOW_HISTORY_STATE_VALUE = 1;
    /**INT value for regular calculation (CALCULATE_INTEGER) program state.*/
    private final int CALCULATE_INTEGER_STATE_VALUE = 0;


    /**Stores current state of the program.*/
    private programStates state;
    /**Placeholder for the Main object.*/
    private static Core core;

    /**The display  module which will be used to show the results/questions/data to user.*/
    private IDisplayModule display;
    /**The input module that allows the user to interact with the program.*/
    private IInputModule input;

    /**Ctor that initializes I/O modules and integral data (integral data for now - this will be changed in exercise 2).*/
    private Core()
    {
        display = new ConsoleDisplay();
        input = new ConsoleInput();
        state = programStates.MENU;
        showMsgToUser = new ShowMsgToUser(display, input);
    }
    /**Enables the process of calculating the integral value.*/
    private void triggerCalculations()
    {
        commandWrapper.newArgList();
        try {
            String calculateCommand = commandWrapper.createCommand(CommandWrapper.commandType.CALCULATE);
            connectionManager.sendMessage(calculateCommand);

            calculateCommand = connectionManager.retreiveMessage();
            display.showData(calculateCommand);
        }
        catch(IOException ex)
        {
            System.out.println("Error while retrieving server answer! \n" + ex.getMessage());
        }
    }
    /**Changes state of the program.
     * @param newState The new state of the program.*/
    private void changeState(programStates newState)
    {
        state = newState;
    }
    /**Changes state of the program.
     * @param newState The new state of the program (int value, check {@link Core}).*/
    private void changeState(int newState)
    {
        switch (newState)
        {
            case CALCULATE_INTEGER_STATE_VALUE:
                state = programStates.CALCULATE_INTEGER;
                return;
            case SHOW_HISTORY_STATE_VALUE:
                state = programStates.DISPLAY_HISTORY;
                return;
            case EXIT_STATE_VALUE:
                state = programStates.EXIT;
                return;
            default:
                //Wrong state. Simply do not change it.
                break;  //Because IntelliJ had problems with return in here. xD
        }
    }

    /**Manages selection of the calculation method process.*/
    private void methodSelection()
    {
        Character selection;
        String accuracy;
        String command = new String();

        try {
            do {
                commandWrapper.newArgList();
                selection = showMsgToUser.askUsrForMethod();
                accuracy = showMsgToUser.askUsrForAccuracy();

                commandWrapper.addArgument(selection.toString());
                commandWrapper.addArgument(accuracy);

                command = commandWrapper.createCommand(CommandWrapper.commandType.SET_METHOD);

                connectionManager.sendMessage(command);
                command = connectionManager.retreiveMessage();
                display.showData(command);                                          //At the end, show the message to the user.

            } while (!commandWrapper.ChkIfAnswerCorrect(command));          //Keep maltreat the user 'till they provide correct data.
        }
        catch(IOException ex)
        {
            System.out.println("Could not retrieve answer from the server (SET_METHOD)! \n" + ex.getMessage());
        }
    }
    /**Manages the process of setting the range of the integral calculation.*/
    private void setIntegralRange()
    {
        String command;

        try {
            do {
                String integralFormula = showMsgToUser.askUsrForFunction();
                Pair<String, String> integralRange = showMsgToUser.askUsrForRange();

                commandWrapper.newArgList();
                commandWrapper.addArgument(integralFormula);
                commandWrapper.addArgument(integralRange.getKey());
                commandWrapper.addArgument(integralRange.getValue());

                command = commandWrapper.createCommand(CommandWrapper.commandType.SET_INTEGRAL);
                connectionManager.sendMessage(command);

                command = connectionManager.retreiveMessage();
                display.showData(command);
            }while(!commandWrapper.ChkIfAnswerCorrect(command));
        }
        catch(IOException ex)
        {
            System.out.println("Could not read answer for the server (SET_INTEGRAL)! " + ex.getMessage());
        }
    }
    /**Manages process of showing the history to the user.*/
    private void showCalculationHistory()
    {
        try {
            commandWrapper.newArgList();
            String command = commandWrapper.createCommand(CommandWrapper.commandType.GET_HISTORY);
            connectionManager.sendMessage(command);

            command = connectionManager.retreiveMessage();
            display.showData(command);                                      //There's no user influence in here - they do not provide any data for this
                                                                            // command so we can assume that it is always correct.
        }
        catch(IOException ex)
        {
            System.out.println("Could not retrieve answer from the server! \n" + ex.getMessage());
        }
    }


    /**
     * Manages selection of one of the options in the menu.
     */
    private void selectMenuOption()
    {
        changeState(showMsgToUser.selectActionFromMenu());
    }

    /**Main loop of the program*/
    private void mainLoop()
    {
        showMsgToUser.printWelcome();
        while(state != programStates.EXIT)
        {
            switch(state) {
                case MENU:
                    selectMenuOption();
                    break;
                case DISPLAY_HISTORY:
                    showCalculationHistory();

                    changeState(programStates.MENU);
                    break;
                case CALCULATE_INTEGER:
                    methodSelection();
                    setIntegralRange();

                    triggerCalculations();

                    changeState(programStates.MENU);
                    break;
                default:
                    changeState(programStates.MENU);    //No idea how did you get here, but get back to the menu this instant!
                    break;
            }
        }
    }

    /**
     * Manages configuration loading from a properties file.
     */
    private void loadConfiguration()
    {
        ConfigLoader configLoader = new ConfigLoader();
        ConfigLoader.loadingResult loadingResult = configLoader.loadConfiguration();

        try {
            showMsgToUser.showUserConfigLoadingInfo(loadingResult);
            connectionManager.setServerPort(configLoader.getUsedPort());
            connectionManager.setServerAddress(configLoader.getServerIP());
            connectionManager.connectToServer();
        }
        catch(IOException ex)
        {
            System.out.println("Could not connect to server! \n" + ex.getMessage() + " \n \n closing client.");
            changeState(programStates.EXIT);
        }
    }
    /**Creates instance of the core, then calls the main loop of the program.*/
    static public void start()
    {
        if (core == null) {
            core = new Core();
        }

        core.loadConfiguration();

        core.mainLoop();
    }
}
