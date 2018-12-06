package pl.polsl.view.userPrompt;

import javafx.util.Pair;
import pl.polsl.utility.dataCheck.StringToNumber;
import pl.polsl.view.display.IDisplayModule;
import pl.polsl.view.input.IInputModule;

/**Class responsible for forming and sending message to the user (via display module).
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/

public class ShowMsgToUser{
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
    public String askUsrForAccuracy()
    {
        String rawAccuracy; //Accuracy that just have been read from the command line.

        display.showData("Specify accuracy: \n");

        rawAccuracy = input.getLine();

        return rawAccuracy;
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

        display.showData("Now input the function: \n");
        inputData = input.getLine();

        return inputData;
    }

    /**Asks user with provided message for input of range part (double number).
     * @param message The message that will be shown to the user.*/
    private String askForDoubleValue(String message)
    {
        String usrInput;

        display.showData(message);
        usrInput = input.getLine();

        return usrInput;
    }

    /**Retrieves from the user the range of the integral that is supposed to be calculated.
     * @return A pair - beginning, end of range.*/
    public Pair<String, String> askUsrForRange()
    {
        String beginning;
        String end;

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
}
