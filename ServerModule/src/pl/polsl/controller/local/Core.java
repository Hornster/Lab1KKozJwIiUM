package pl.polsl.controller.local;

import pl.polsl.server.ConfigLoader;
import pl.polsl.server.ConnectionManager;
import pl.polsl.model.exceptions.IntegralCalculationException;
import pl.polsl.model.queryHistory.QueryHistory;
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
    /**Manages calculation performing and data assigning (needed for the calculations).*/
    private CalculationModule calculationModule;
    /**States of the program.*/
    private enum programStates{EXIT, RETRIEVE_COMMAND, DISPLAY_HISTORY, MENU}

    /**Int value for the EXIT program state.*/
    private final int EXIT_STATE_VALUE = 2;
    /**Int value for DISPLAY_HISTORY program state.*/
    private final int SHOW_HISTORY_STATE_VALUE = 1;
    /**INT value for regular calculation (RETRIEVE_COMMAND) program state.*/
    private final int CALCULATE_INTEGER_STATE_VALUE = 0;


    /**Stores current state of the program.*/
    private programStates state;
    /**Placeholder for the Main object.*/
    private static Core core;


    /**Ctor that initializes I/O modules and integral data (integral data for now - this will be changed in exercise 2).*/
    private Core()
    {
        state = programStates.MENU;
        calculationModule = new CalculationModule();
        queryManager = new QueryManager();

        calculationModule.addListener(queryManager);
    }
    /**Enables the process of calculating the integral value.*/
    private void triggerCalculations()
    {
        /*
        try {
            double result = calculationModule.performCalculation();

            connectionManager.showCalculationResult(result);
        }
        catch(IntegralCalculationException ex)
        {
            display.showData(ex.getMessage());
        }*/
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
                state = programStates.RETRIEVE_COMMAND;
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
    {/*
        char selection;
        do {
            selection = connectionManager.askUsrForMethod();
        }while(!calculationModule.selectMethod(selection));*/
    }
    /**Manages the process of setting the range of the integral calculation.*/
    private void setIntegralRange()
    {
        /*calculationModule.assignNewIntegralRange(connectionManager.askUsrForRange());*/
    }
    /**Manages process of showing the history to the user.*/
    private void showCalculationHistory()
    {/*
        QueryHistory history = queryManager.getQueryHistory();
        if(history.size() <= 0)
        {
            connectionManager.showNoCalcHistoryFound();
        }
        else
        {
            for (Object q : history) {
                connectionManager.showCalculationHistory(q.toString());
            }
        }*/
    }

    /**Main loop of the program*/
    private void mainLoop()
    {
        connectionManager.printWelcome();
        while(state != programStates.EXIT)
        {
            switch(state) {
                case DISPLAY_HISTORY:
                    showCalculationHistory();

                    changeState(programStates.MENU);
                    break;
                case RETRIEVE_COMMAND:
                    methodSelection();/*
                    calculationModule.setAccuracy(connectionManager.askUsrForAccuracy());
                    calculationModule.setFunction(connectionManager.askUsrForFunction());
                    setIntegralRange();*/

                    triggerCalculations();

                    changeState(programStates.MENU);
                    break;
                default:
                    changeState(programStates.MENU);    //No idea how did you get here, but get back to the menu this instant!
                    break;
            }
        }
    }
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
