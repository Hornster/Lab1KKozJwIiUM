package pl.polsl;

import javafx.util.Pair;
import pl.polsl.data.IntegralData;
import pl.polsl.display.*;
import pl.polsl.exceptions.NoFunctionAssignedException;
import pl.polsl.input.ConsoleInput;
import pl.polsl.input.IInputModule;
import pl.polsl.output.AskUser;
import pl.polsl.utils.*;

/**Contains entrance point for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.4*/

public class Main {

    /**Entrance point for the program.
     * @param args No args are taken in.*/
    public static void main(String[] args) {
        Core.start();
    }
}