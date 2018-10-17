package lab1proj;

import com.sun.istack.internal.NotNull;
import lab1proj.data.IntegralData;
import lab1proj.display.*;
import lab1proj.input.ConsoleInput;
import lab1proj.input.IInputModule;
import lab1proj.utils.*;
/**Contains entrance point, as well as the core methods for the program.*/
public class Main {
    char lastMethodDecision = '\0';
    /**States of the program.*/
    enum programStates{EXIT, WORKING}
    programStates state;
    /**Placeholder for the Main object.*/
    static Main main;
    /**The integral to calculate.*/
    IntegralData integral;
    /**The display  module which will be used to show the results/questions/data to user.*/
    IDisplayModule display;
    /**The input module that allows the user to interact with the program.*/
    IInputModule input;
    /**Field for the calculation method (check @lab1proj.utils)*/
    IntegralCalculator calculator;
    public Main()
    {
        display = new ConsoleDisplay();
        input = new ConsoleInput();
        state = programStates.WORKING;
        integral = new IntegralData(0, 1.8);
    }
    /**Entrance point for the program.
     * @param args No args are taken in.*/
    public static void main(String[] args) throws InterruptedException {
        Main.main = new Main();
        main.mainLoop();
    }
    private void printWelcome()
    {
        display.ShowData("Hllo there! \n\n\n");
    }
    /**Pops up a message when user inputs wrong accuracy (negative number, for example).*/
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
        int desiredAccuracy;

        display.ShowData("Specify accuracy: \n");

        do {
            desiredAccuracy = input.getNumber();
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
    /**Responsible for selecting method of calculating the integral.
     * @param methodCode Char code of the method. 't' stands for Trapezoidal, 's' for Square method.*/
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

            state = programStates.EXIT;
        }
    }
}
