package pl.polsl.controller;

import javafx.util.Pair;
import pl.polsl.view.display.ConsoleDisplay;
import pl.polsl.view.display.IDisplayModule;
import pl.polsl.view.input.ConsoleInput;
import pl.polsl.view.input.IInputModule;
import pl.polsl.view.userPrompt.ShowMsgToUser;

/**Contains the core methods for the program.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.2*/
public class Core {
    /**Responsible for sending messages to the user.*/
    private ShowMsgToUser showMsgToUser;
    /**States of the program.*/
    private enum programStates{EXIT, CALCULATE_INTEGER, DISPLAY_HISTORY, MENU}

    /**Int value for the EXIT program state.*/
    private final int EXIT_STATE_VALUE = 2;
    /**Int value for DISPLAY_HISTORY program state.*/
    private final int SHOW_HISTORY_STATE_VALUE = 1;
    /**INT value for regular calculation (CALCULATE_INTEGER) program state.*/
    private final int CALCULATE_INTEGER_STATE_VALUE = 0;


    /**Stores current state of the program.*/
    private programStates state;
    /**Placeholder for the Main object.*/
    private static Core core;

    /**The display  module which will be used to show the results/questions/data to user.*/
    private IDisplayModule display;
    /**The input module that allows the user to interact with the program.*/
    private IInputModule input;

    /**Ctor that initializes I/O modules and integral data (integral data for now - this will be changed in exercise 2).*/
    private Core()
    {
        display = new ConsoleDisplay();
        input = new ConsoleInput();
        state = programStates.MENU;
        showMsgToUser = new ShowMsgToUser(display, input);
    }
    /**Enables the process of calculating the integral value.*/
    private void triggerCalculations()
    {
        //TODO send command CALCULATE
    }
    /**Changes state of the program.
     * @param newState The new state of the program.*/
    private void changeState(programStates newState)
    {
        state = newState;
    }
    /**Changes state of the program.
     * @param newState The new state of the program (int value, check {@link Core}).*/
    private void changeState(int newState)
    {
        switch (newState)
        {
            case CALCULATE_INTEGER_STATE_VALUE:
                state = programStates.CALCULATE_INTEGER;
                return;
            case SHOW_HISTORY_STATE_VALUE:
                state = programStates.DISPLAY_HISTORY;
                return;
            case EXIT_STATE_VALUE:
                state = programStates.EXIT;
                return;
            default:
                //Wrong state. Simply do not change it.
                break;  //Because IntelliJ had problems with return in here. xD
        }
    }

    /**Manages selection of the calculation method process.*/
    private char methodSelection()
    {
        char selection;

        selection = showMsgToUser.askUsrForMethod();

        return selection;
    }
    /**Manages the process of setting the range of the integral calculation.*/
    private Pair<String, String> setIntegralRange()
    {
        return showMsgToUser.askUsrForRange();
    }
    /**Manages process of showing the history to the user.*/
    private void showCalculationHistory()
    {
        //TODO prepare Get_histroy command and send it to server.
    }


    /**
     * Manages selection of one of the options in the menu.
     */
    private void selectMenuOption()
    {
        changeState(showMsgToUser.selectActionFromMenu());
    }

    /**Main loop of the program*/
    private void mainLoop()
    {
        showMsgToUser.printWelcome();
        while(state != programStates.EXIT)
        {
            switch(state) {
                case MENU:
                    selectMenuOption();
                    break;
                case DISPLAY_HISTORY:
                    showCalculationHistory();

                    changeState(programStates.MENU);
                    break;
                case CALCULATE_INTEGER:
                    methodSelection();
                    calculationModule.setAccuracy(showMsgToUser.askUsrForAccuracy());
                    calculationModule.setFunction(showMsgToUser.askUsrForFunction());
                    setIntegralRange();

                    triggerCalculations();

                    changeState(programStates.MENU);
                    break;
                default:
                    changeState(programStates.MENU);    //No idea how did you get here, but get back to the menu this instant!
                    break;
            }
        }
    }
    /**Creates instance of the core, then calls the main loop of the program.*/
    static public void start()
    {
        if (core == null) {
            core = new Core();
        }

        core.mainLoop();
    }
}
