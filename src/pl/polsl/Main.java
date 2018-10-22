package pl.polsl;

import com.sun.istack.internal.NotNull;
import pl.polsl.data.IntegralData;
import pl.polsl.display.*;
import pl.polsl.input.ConsoleInput;
import pl.polsl.input.IInputModule;
import pl.polsl.utils.*;

/**Contains entrance point, as well as the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/

public class Main {
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
    public Main()
    {
        display = new ConsoleDisplay();
        input = new ConsoleInput();
        state = programStates.WORKING;
        integral = new IntegralData(0, 1.8);
    }
    /**Entrance point for the program.
     * @param args No args are taken in.*/
    public static void main(String[] args) {
        Main.core = new Main();
        core.mainLoop();
    }
    /**Shows a welcoming message to the user.*/
    private void printWelcome()
    {
        display.ShowData("Hllo there! \n\n\n");
    }
    /**Pops up a warning message when user inputs wrong accuracy (negative number, for example).*/
    private void shoutWrongAccuracy()
    {
        display.ShowData("Unfortunately, you failed miserably. Accuracy must be higher " +
                "than 0 and be an integer type number! Try again: ");
    }
    /**Retrieves from the user info about desired accuracy of calculations.
     * @param calculator The calculating method used for calculation, which's accuracy shall change.*/
    private void askUsrForAccuracy(@NotNull IntegralCalculator calculator)
    {
        boolean correctInput = false;
        String rawAccuracy; //Accuracy that just have been read from the command line.
        int desiredAccuracy = 1;

        display.ShowData("Specify accuracy: \n");

        do {
            rawAccuracy = input.getLine();
            if(!StringToNumber.TryStringToInt(rawAccuracy)) {   //Test if the string has a number in it...
                shoutWrongAccuracy();//...if not - report that to user and wait another input try.
                continue;
            }

            desiredAccuracy = Integer.parseInt(rawAccuracy);

            if(desiredAccuracy > 0)
                correctInput = true;
            else
                shoutWrongAccuracy();
        }
        while(!correctInput);

        calculator.setPrecision(desiredAccuracy);
    }
    /**Retrieves from the user information about which method shall be used for calculations.*/
    private void askUsrForMethod()
    {
        String inputData;

        do {
            display.ShowData("What method to use (t for trapezoidal, s for square): \n");
            inputData = input.getLine();

            /*if(inputData.length() <= 0)
            {
                display.ShowData("Input is needed. Insert t for trapezoidal, s for square method: \n");
                continue;   //User failed miserably and needs to be asked for another input.
            }*/

            inputData = inputData.toLowerCase();
        }while(!selectMethod(inputData.charAt(0)));

    }
    /**Enables the process of calculating the integral value.*/
    private void triggerCalculations()
    {
        double result = calculator.calculateIntegral();

        display.ShowData("The result of calculating the integral: ");
        display.ShowData(result);
        display.ShowData("\n");
    }
    /**Changes state of the program.
     * @param newState The new state of the program.*/
    private void changeState(programStates newState)
    {
        state= newState;
    }
    /**Asks user if they still want to work with the program.*/
    private void askUsrForContinue()
    {
        display.ShowData("Continue (y if yes, anything otherwise)?");

        String answer = input.getLine();
        answer.toLowerCase();

        if(answer.charAt(0)== 'y') {
            changeState(programStates.WORKING);
        }
        else {
            changeState(programStates.EXIT);
        }
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
    /**Main loop of the program*/
    private void mainLoop()
    {
        printWelcome();
        while(state != programStates.EXIT)
        {
            askUsrForMethod();
            askUsrForAccuracy(calculator);

            triggerCalculations();

            askUsrForContinue();
        }
    }
}
