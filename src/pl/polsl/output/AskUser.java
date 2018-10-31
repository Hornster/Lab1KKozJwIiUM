package pl.polsl.output;

import javafx.util.Pair;
import pl.polsl.display.IDisplayModule;
import pl.polsl.input.IInputModule;
import pl.polsl.utils.DataChk;
import pl.polsl.utils.StringToNumber;

/**Class responsible for forming and sending message to the user (via display module).
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/

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
    /**Informs the user how should the range input look like.*/
    private void shoutWrongRange()
    {
        display.showData("Unfortunately, you failed critically. Range must be a real number!");
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

        do {
            display.showData("Now input the function: \n");
            inputData = input.getLine();
            if(!DataChk.validateFunctionSyntax(inputData))
            {
                shoutWrongFunction();//User made some errors in the syntax - inform them about that and ask again for input.
            }
            else
            {
                break; //Exit the loop - the user managed to input correct function.
            }
        }while(true);

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
    /**Asks user with provided message for input of range part (double number).
     * @param message The message that will be shown to the user.*/
    private double askForDoubleValue(String message)
    {
        String usrInput;
        double readyValue;
        do {
            display.showData(message);
            usrInput = input.getLine();
            if(!StringToNumber.tryStringToDouble(usrInput))
            {
                shoutWrongRange();
            }
            else
            {
                readyValue = Double.parseDouble(usrInput);
                break;
            }
        }while(true);

        return readyValue;
    }

    /**Retrieves from the user the range of the integral that is supposed to be calculated.
     * @return A pair - beginning, end of range.*/
    public Pair<Double, Double> askUsrForRange()
    {
        double beginning;
        double end;

        beginning = askForDoubleValue("Please input the beginning of the range: \n");
        end = askForDoubleValue("Please input the end of the range: \n");

        return new Pair<>(beginning, end);
    }

    /**Informs the user about errors in the provided method's syntax.*/
    private void shoutWrongFunction()
    {
        display.showData("Unfortunately the function you have provided has errors in syntax. Please try again. \n");
    }
}
