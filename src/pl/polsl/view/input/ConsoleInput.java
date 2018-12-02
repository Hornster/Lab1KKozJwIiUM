package pl.polsl.view.input;

import java.util.Scanner;
/**Performs input action by reading from the console.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0*/
public class ConsoleInput implements IInputModule {
    /**Scanner hooked to the System.in.*/
    Scanner input = new Scanner(System.in);

    /**Reads a single line from the System.in. Also, makes sure that at least one character is read.
     * @return A line of text, containing at least one character.*/
    @Override
    public String getLine() {
        String line;
        do {
            line = input.nextLine();
        }while(line.length() == 0);

        return line;
    }
}
