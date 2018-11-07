package pl.polsl;

import pl.polsl.data.QueryHistory;
import pl.polsl.display.ConsoleDisplay;
import pl.polsl.display.IDisplayModule;
import pl.polsl.exceptions.NoFunctionAssignedException;
import pl.polsl.input.ConsoleInput;
import pl.polsl.input.IInputModule;
import pl.polsl.userPrompt.AskUser;
import pl.polsl.service.CalculationModule;
import pl.polsl.utils.QueryManager;

/**Contains the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
class Core {
    /**Stores information about already processed approximation queries.*/
    private QueryManager queryManager;
    /**Responsible for sending messages to the user.*/
    private AskUser askUser;
    /**Manages calculation performing and data assigning (needed for the calculations).*/
    private CalculationModule calculationModule;
    /**States of the program.*/
    private enum programStates{EXIT, WORKING}
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
        state = programStates.WORKING;
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
        catch(NoFunctionAssignedException ex)
        {
            display.showData(ex.getMessage());
        }
    }
    /**Changes state of the program.
     * @param newState The new state of the program.*/
    private void changeState(programStates newState)
    {
        state= newState;
    }

    /**Manages selection of the calculation method process.*/
    private void methodSelection()
    {
        char selection;
        do {
            selection = askUser.askUsrForMethod();
        }while(!calculationModule.selectMethod(selection));
    }
    /**Manages asking the user if they still want to perform calculations or quit.*/
    private void chkContinuation()
    {
        char decision = askUser.askUsrForContinue();

        decision = Character.toLowerCase(decision);

        if(decision == 'y') {
            changeState(Core.programStates.WORKING);
        }
        else {
            changeState(Core.programStates.EXIT);
        }
    }
    /**Manages the process of setting the range of the integral calculation.*/
    private void setIntegralRange()
    {
        calculationModule.AssignNewIntegralRange(askUser.askUsrForRange());
    }
    /**Main loop of the program*/
    private void mainLoop()
    {
        askUser.printWelcome();
        while(state != programStates.EXIT)
        {
            methodSelection();
            calculationModule.setAccuracy(askUser.askUsrForAccuracy());
            calculationModule.setFunction(askUser.askUsrForFunction());
            setIntegralRange();

            triggerCalculations();

            chkContinuation();
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
