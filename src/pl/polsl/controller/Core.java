package pl.polsl.controller;

import pl.polsl.model.QueryHistory;
import pl.polsl.view.display.ConsoleDisplay;
import pl.polsl.view.display.IDisplayModule;
import pl.polsl.model.exceptions.IntegralCalculationException;
import pl.polsl.view.input.ConsoleInput;
import pl.polsl.view.input.IInputModule;
import pl.polsl.view.userPrompt.ShowMsgToUser;
import pl.polsl.model.QueryManager;

/**Contains the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.2*/
public class Core {
    /**Stores information about already processed approximation queries.*/
    private QueryManager queryManager;
    /**Responsible for sending messages to the user.*/
    private ShowMsgToUser showMsgToUser;
    /**Manages calculation performing and data assigning (needed for the calculations).*/
    private CalculationModule calculationModule;
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
        calculationModule = new CalculationModule();
        showMsgToUser = new ShowMsgToUser(display, input);
        queryManager = new QueryManager();

        calculationModule.addListener(queryManager);
    }
    /**Enables the process of calculating the integral value.*/
    private void triggerCalculations()
    {
        try {
            double result = calculationModule.performCalculation();

            display.showData("The result of calculating the integral: ");
            display.showData(result);
            display.showData("\n");
        }
        catch(IntegralCalculationException ex)
        {
            display.showData(ex.getMessage());
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
        char selection;
        do {
            selection = showMsgToUser.askUsrForMethod();
        }while(!calculationModule.selectMethod(selection));
    }
    /**Manages the process of setting the range of the integral calculation.*/
    private void setIntegralRange()
    {
        calculationModule.assignNewIntegralRange(showMsgToUser.askUsrForRange());
    }
    /**Manages process of showing the history to the user.*/
    private void showCalculationHistory()
    {
        QueryHistory history = queryManager.getQueryHistory();
        if(history.size() <= 0)
        {
            showMsgToUser.showNoCalcHistoryFound();
        }
        else
        {
            for (Object q : history) {
                showMsgToUser.showCalculationHistory(q.toString());
            }
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
                    calculationModule.setAccuracy(showMsgToUser.askUsrForAccuracy());
                    calculationModule.setFunction(showMsgToUser.askUsrForFunction());
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
    /**Creates instance of the core, then calls the main loop of the program.*/
    static public void start()
    {
        if (core == null) {
            core = new Core();
        }

        core.mainLoop();
    }
}
