package lab1proj.input;

import java.util.Scanner;

public class ConsoleInput implements IInputModule {
    Scanner input = new Scanner(System.in);
    @Override
    public int getNumber() {
        int value = 0;
        value = input.nextInt();

        return value;
    }

    @Override
    public String getLine() {
        String line;
        line = input.nextLine();

        return line;
    }
}
