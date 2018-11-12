package pl.polsl;

import pl.polsl.data.QueryHistory;
import pl.polsl.display.ConsoleDisplay;
import pl.polsl.display.IDisplayModule;
import pl.polsl.exceptions.IntegralCalculationException;
import pl.polsl.input.ConsoleInput;
import pl.polsl.input.IInputModule;
import pl.polsl.userPrompt.AskUser;
import pl.polsl.service.CalculationModule;
import pl.polsl.utils.QueryManager;

/**Contains the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.2*/
class Core {
    /**Stores information about already processed approximation queries.*/
    private QueryManager queryManager;
    /**Responsible for sending messages to the user.*/
    private AskUser askUser;
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
        askUser = new AskUser(display, input);
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
     * @param newState The new state of the program (int value, check {@link pl.polsl.Core}).*/
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
            selection = askUser.askUsrForMethod();
        }while(!calculationModule.selectMethod(selection));
    }
    /**Manages the process of setting the range of the integral calculation.*/
    private void setIntegralRange()
    {
        calculationModule.assignNewIntegralRange(askUser.askUsrForRange());
    }
    /**Manages process of showing the history to the user.*/
    private void showCalculationHistory()
    {
        QueryHistory history = queryManager.getQueryHistory();
        if(history.size() <= 0)
        {
            display.showData("No queries recorded yet, Darling! \n");
        }
        else {
            //TODO: When DNS returns,check why on earth does it have an Object, not a SingleQuery...>.>
            for (Object q : history) {
                display.showData(q.toString());
                display.showData("\n");
            }
        }
    }


    /**
     * Manages selection of one of the options in the menu.
     */
    private void selectMenuOption()
    {
        changeState(askUser.selectActionFromMenu());
    }

    /**Main loop of the program*/
    private void mainLoop()
    {
        askUser.printWelcome();
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
                    calculationModule.setAccuracy(askUser.askUsrForAccuracy());
                    calculationModule.setFunction(askUser.askUsrForFunction());
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
    static void start()
    {
        if (core == null) {
            core = new Core();
        }

        core.mainLoop();
    }
}
