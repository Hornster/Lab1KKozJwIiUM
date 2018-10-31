package pl.polsl;

import pl.polsl.data.IntegralData;
import pl.polsl.display.*;
import pl.polsl.exceptions.NoFunctionAssignedException;
import pl.polsl.input.ConsoleInput;
import pl.polsl.input.IInputModule;
import pl.polsl.output.AskUser;
import pl.polsl.utils.*;

/**Contains entrance point, as well as the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.3*/

public class Main {
    AskUser askUser;
    /**The last decision (about used integral calc. method) made by the user*/
    private char lastMethodDecision = '\0';
    /**States of the program.*/
    private enum programStates{EXIT, WORKING}
    /**Stores current state of the program.*/
    private programStates state;
    /**Placeholder for the Main object.*/
    private static Main core;
    /**The integral to calculate.*/
    private IntegralData integral;
    /**The display  module which will be used to show the results/questions/data to user.*/
    private IDisplayModule display;
    /**The input module that allows the user to interact with the program.*/
    private IInputModule input;
    /**Field for the calculation method (check @pl.polsl.pl.polsl.tests.utils)*/
    private IntegralCalculator calculator;
    /**Ctor that initializes I/O modules and integral data (integral data for now - this will be changed in exercise 2).*/
    private Main()
    {
        display = new ConsoleDisplay();
        input = new ConsoleInput();
        state = programStates.WORKING;
        integral = new IntegralData(0, 1.8);
        askUser = new AskUser(display, input);
    }
    /**Entrance point for the program.
     * @param args No args are taken in.*/
    public static void main(String[] args) {
        Main.core = new Main();
        core.mainLoop();
    }



    /**Assigns input function to integral.
     * @param functionSyntax Syntax of newly input function.*/
    private void setFunction(String functionSyntax)
    {
        integral.setIntegralFunc(functionSyntax);
    }

    /**Enables the process of calculating the integral value.*/
    private void triggerCalculations()
    {
        try {
            double result = calculator.calculateIntegral();

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

    /**Responsible for selecting method of calculating the integral.
     * @param methodCode Char code of the method. 't' stands for Trapezoidal, 's' for Square method.
     * @return TRUE if the method has been set. FALSE otherwise.*/
    private boolean selectMethod(char methodCode)
    {

        if(lastMethodDecision == methodCode)
            return true; //No need to change algorithm since user selected the previously chosen.
        switch (methodCode)
        {
            case 't':
                calculator = new TrapezoidMethod(integral);
                lastMethodDecision = methodCode;
                return true;
            case 's':
                calculator = new SquareMethod(integral);
                lastMethodDecision = methodCode;
                return true;
            default:
                return false;
        }


    }
    /**Manages selection of the calculation method process.*/
    private void methodSelection()
    {
        char selection;
        do {
            selection = askUser.askUsrForMethod();
        }while(!selectMethod(selection));
    }
    /**Manages asking the user if they still want to perform calculations or quit.*/
    private void chkContinuation()
    {
        char decision = askUser.askUsrForContinue();

        decision = Character.toLowerCase(decision);

        if(decision == 'y') {
            changeState(Main.programStates.WORKING);
        }
        else {
            changeState(Main.programStates.EXIT);
        }
    }

    /**Main loop of the program*/
    private void mainLoop()
    {
        askUser.printWelcome();
        while(state != programStates.EXIT)
        {
            methodSelection();
            calculator.setPrecision(askUser.askUsrForAccuracy());
            setFunction(askUser.askUsrForFunction());

            triggerCalculations();

            chkContinuation();
        }
    }
}
