package lab1proj.input;

import java.util.Scanner;
/**Performs input action by reading from the console.*/
public class ConsoleInput implements IInputModule {
    /**Scanner hooked to the System.in.*/
    Scanner input = new Scanner(System.in);

    /**Reads a single line from the System.in. Also, makes sure that at least one character is read.
     * @return*/
    @Override
    public String getLine() {
        String line;
        do {
            line = input.nextLine();
        }while(line.length() == 0);

        return line;
    }
}
