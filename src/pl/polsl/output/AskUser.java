package pl.polsl.output;

import com.sun.istack.internal.NotNull;
import pl.polsl.Main;
import pl.polsl.display.IDisplayModule;
import pl.polsl.input.IInputModule;
import pl.polsl.utils.DataChk;
import pl.polsl.utils.IntegralCalculator;
import pl.polsl.utils.StringToNumber;

/**Class responsible for forming and sending message to the user (via display module).*/
public class AskUser {
    private IDisplayModule display;
    private IInputModule input;

    public AskUser(IDisplayModule displayModule, IInputModule inputModule)
    {
        display = displayModule;
        input = inputModule;
    }

    /**Shows a welcoming message to the user.*/
    public void printWelcome()
    {
        display.showData("Hllo there! \n\n\n");
    }
    /**Pops up a warning message when user inputs wrong accuracy (negative number, for example).*/
    private void shoutWrongAccuracy()
    {
        display.showData("Unfortunately, you failed miserably. Accuracy must be higher " +
                "than 0 and be an integer type number! Try again: ");
    }
    /**Retrieves from the user info about desired accuracy of calculations.
     */
    public int askUsrForAccuracy()
    {
        boolean correctInput = false;
        String rawAccuracy; //Accuracy that just have been read from the command line.
        int desiredAccuracy = 1;

        display.showData("Specify accuracy: \n");

        do {
            rawAccuracy = input.getLine();
            if(!StringToNumber.tryStringToInt(rawAccuracy)) {   //Test if the string has a number in it...
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

        return desiredAccuracy;
    }
    /**Retrieves from the user information about which method shall be used for calculations.
     * @return First letter of user's decision.*/
    public char askUsrForMethod()
    {
        String inputData;

        display.showData("What method to use (t for trapezoidal, s for square): \n");
        inputData = input.getLine();

        inputData = inputData.toLowerCase();
        return inputData.charAt(0);
    }
    /**Retrieves from the user the function which shall be used in calculations.
     * @return Valid function syntax.*/
    public String askUsrForFunction()
    {
        String inputData;
        boolean syntaxCorrect = false;

        do {
            display.showData("Now input the function: \n");
            inputData = input.getLine();
        }while(!DataChk.validateFunctionSyntax(inputData));

        return inputData;
    }
    /**Asks user if they still want to work with the program.
     * @return User's decision.*/
    public char askUsrForContinue()
    {
        display.showData("Continue (y if yes, anything otherwise)?");

        String answer = input.getLine();

        return answer.charAt(0);
    }
    /**Informs the user about errors in the provided method's syntax.*/
    private void shoutWrongFunction()
    {
        display.showData("Unfortunately the function you have provided has errors in syntax. Please try again: \n");
    }
}
