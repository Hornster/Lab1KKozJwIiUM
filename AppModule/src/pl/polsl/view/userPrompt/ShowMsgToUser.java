package pl.polsl.view.userPrompt;

import javafx.util.Pair;
import pl.polsl.view.display.IDisplayModule;
import pl.polsl.view.input.IInputModule;
import pl.polsl.utility.dataCheck.DataChk;
import pl.polsl.utility.dataCheck.StringToNumber;

/**Class responsible for forming and sending message to the user (via display module).
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/

public class ShowMsgToUser {
    /**Reference to the module that displays stuff to the user.*/
    private IDisplayModule display;
    /**Reference to the module that grabs the input from the user.*/
    private IInputModule input;


    public ShowMsgToUser(IDisplayModule displayModule, IInputModule inputModule)
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

    /**
     * Ask the user for decision about what shall the program do.
     * @return Integer in form of a string (assured).
     */
    public int selectActionFromMenu()
    {
        String data;
        do {
            display.showData("Please select action: \n");
            display.showData("[0]Calculate integral \n");
            display.showData("[1]Show all queries made during this session \n");
            display.showData("[2]Exit \n");

            data = input.getLine();
        }while(!StringToNumber.tryStringToInt(data));

        return Integer.parseInt(data);
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

    /**
     * Shows passed string describing given query to the user.
     * @param singleQuery String describing a single query.
     */
    public void showCalculationHistory(String singleQuery)
    {
        display.showData(singleQuery);
        display.showData("\n");
    }

    /**
     * Shows info to the user informing that there's no query history recorded yet.
     */
    public void showNoCalcHistoryFound()
    {
        display.showData("No queries recorded yet, Darling! \n");
    }

    /**
     * Shows to the user the calculation result.
     * @param result Value calculated by the program.
     */
    public void showCalculationResult(double result)
    {
        display.showData("The result of calculating the integral: ");
        display.showData(result);
        display.showData("\n");
    }

    /**Informs the user about errors in the provided method's syntax.*/
    private void shoutWrongFunction()
    {
        display.showData("Unfortunately the function you have provided has errors in syntax. Please try again. \n");
    }
}
