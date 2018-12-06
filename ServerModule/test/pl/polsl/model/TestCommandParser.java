package pl.polsl.model;

import org.junit.Assert;
import org.junit.Test;
import pl.polsl.server.CommandParser;

public class TestCommandParser {
    private String command;
    private ServerCommand parsedCommand;
    private CommandParser cmdParser = new CommandParser();


    @Test
    public void testNotFoundProperParser()
    {
        command = "POOTIS";
        parsedCommand = cmdParser.ParseCommand(command);
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());

        command = "";
        parsedCommand = cmdParser.ParseCommand(command);
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType() );
    }
    @Test
    public void testHelpCommandParser()
    {
        command = "help";
        parsedCommand = cmdParser.ParseCommand(command);

        Assert.assertEquals(CommandParser.commandType.HELP, parsedCommand.getCommandType() );

        command = " help ";
        parsedCommand = cmdParser.ParseCommand(command);

        Assert.assertEquals(CommandParser.commandType.HELP, parsedCommand.getCommandType());

    }
    @Test
    public void testCalculateCommandParser()
    {
        parsedCommand = cmdParser.ParseCommand("calculate");
        Assert.assertEquals(CommandParser.commandType.CALCULATE, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("caLCULatE");
        Assert.assertEquals(CommandParser.commandType.CALCULATE, parsedCommand.getCommandType());
    }
    @Test
    public void testGetHistoryParser()
    {
        parsedCommand = cmdParser.ParseCommand("get_HISTORY");
        Assert.assertEquals(CommandParser.commandType.GET_HISTORY, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("GETHISTORY");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("GET_history");
        Assert.assertEquals(CommandParser.commandType.GET_HISTORY, parsedCommand.getCommandType());
    }
    @Test
    public void testSetMethodParser()
    {
        parsedCommand = cmdParser.ParseCommand("set_method t, 10000");
        Assert.assertEquals(CommandParser.commandType.SET_METHOD, parsedCommand.getCommandType());
        Assert.assertEquals(2, parsedCommand.getData().size());
        Assert.assertEquals("t", parsedCommand.getData().get(0));
        Assert.assertEquals("10000", parsedCommand.getData().get(1));

        parsedCommand= cmdParser.ParseCommand("setMethod s, 90");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());

        parsedCommand = cmdParser.ParseCommand("set_method r, 90");
        Assert.assertEquals(CommandParser.commandType.SET_METHOD, parsedCommand.getCommandType());
        Assert.assertEquals(2, parsedCommand.getData().size());
        Assert.assertEquals("r", parsedCommand.getData().get(0));
        Assert.assertEquals("90", parsedCommand.getData().get(1));

        parsedCommand = cmdParser.ParseCommand("set_method r, 0");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_method r, -1");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_method r, -10");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());

        parsedCommand = cmdParser.ParseCommand("set_method r, ");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_method r");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_method");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());

        parsedCommand = cmdParser.ParseCommand("set_method t, 100,");
        Assert.assertEquals(CommandParser.commandType.SET_METHOD, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_method r 100");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_method, r, 100");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
    }
    @Test
    public void testSetIntegralParser()
    {
        int testsAmount = 5;
        String[]correctFormulas = {"f(x)=x^2+5", "f(x)=x^2+1/x", "f(X)=1", "f(Y)=42+84*Y+Y^2", "f(z)=z"};
        String[]beginnings = {"11.0", "-11.0", "0", "12", "12.1"};
        String[]ends = {"23", "-1.5", "0.0", "230", "-123"};
        for(int i = 0; i < testsAmount; i++)
        {
            parsedCommand = cmdParser.ParseCommand("set_integral " + correctFormulas[i] + ',' + beginnings[i] + ',' + ends[i]);
            Assert.assertEquals(CommandParser.commandType.SET_INTEGRAL, parsedCommand.getCommandType());
            Assert.assertEquals(3, parsedCommand.getData().size());
            String checkedFormula = correctFormulas[i].toLowerCase();
            Assert.assertEquals(checkedFormula, parsedCommand.getData().get(0));
            Assert.assertEquals(beginnings[i], parsedCommand.getData().get(1));
            Assert.assertEquals(ends[i], parsedCommand.getData().get(2));
        }
        String[]mixedFormulas = {"f(x)x^2+5", "f(x)=", "(X)=1", "fY) = 42+84Y+1Y^2", "f(z=z"};
        String beginning = "11.0";
        String end = "23";
        for(int i = 0; i < testsAmount; i++)
        {
            parsedCommand = cmdParser.ParseCommand("set_integral " + mixedFormulas[i] + beginning + end);
            Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        }

        parsedCommand = cmdParser.ParseCommand("set_integral " + correctFormulas[0] + ",12");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_integral " + correctFormulas[0]);
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_integral");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_integral " + correctFormulas[0] + ",12" + " ,a");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_integral " + correctFormulas[0] + ",a" + ", 12");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
        parsedCommand = cmdParser.ParseCommand("set_integral " + "12" + ",12" + ",12");
        Assert.assertEquals(CommandParser.commandType.INCORRECT, parsedCommand.getCommandType());
    }
    @Test
    public void testDisconnectParser()
    {
        parsedCommand = cmdParser.ParseCommand("Disconnect");

        Assert.assertEquals(CommandParser.commandType.DISCONNECT, parsedCommand.getCommandType() );
    }
    @Test
    public void testSeveralCommands()
    {
        parsedCommand = cmdParser.ParseCommand("Help Disconnect");

        Assert.assertEquals(CommandParser.commandType.HELP, parsedCommand.getCommandType() );
    }
}
