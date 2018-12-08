package pl.polsl.clientside;

import javafx.util.Pair;
import pl.polsl.utility.dataCheck.ParseModifyString;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/** Loads configuration data for the server from a .properties file.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0
 */
public class ConfigLoader {
    final char separatorChar = ':';
    /**Possible resulting states of loading properties from the file.
     * NOT_FOUND_OR_INCORRECT - The property was recognized but corrupted.
     * NOT_FOUND - Property was not recognized by given parser.
     * FOUND - Property was recognized and correctly processed.*/
    private enum propertyLoadingState{INCORRECT, NOT_FOUND, FOUND}
    /**
     * Stores possible loading states.
     * NOT_PERFORMED - loadConfiguration was not called, even.
     * FILE_NOT_FOUND - unable to find .properties file. All fields will stay as defaults.
     * PARTIAL - not all values were loaded correctly (might be missing or effect of an error).
     *          Data not loaded is set to default values.
     * FULL - Everything was successfully loaded.
     */
    public enum loadingResult{NOT_PERFORMED, FILE_NOT_FOUND, PARTIAL, FULL}
    /**Path to configuration file, together with an extension.     */
    private final String configFile = "clientConfig.properties";
    /**String with name of server port property.*/
    private final String propertyServerPort = "server-port";
    /**String with name of server ip property.*/
    private final String propertyServerIP = "server-ip";
    /**Default port used by the server.*/
    private final String defaultPort = "8090";
    /**Default ip address of the server which we will connect to.*/
    private final String defaultServerIP = "localhost";
    /**Stores all the parsers, allowing for iteration based checking of read from properties file properties (sequence is not essential).*/
    private List<ReadPropertyValueMethod> propertyParsers = new LinkedList<>();
    /**During loading the properties from the property file, this will get */
    private Map<String, propertyLoadingState> propertiesLoadingStatus = new HashMap<>();

    public ConfigLoader()
    {
        propertyParsers.add(readServerPort);
        propertyParsers.add(readServerIP);

        propertiesLoadingStatus.put(propertyServerPort, propertyLoadingState.NOT_FOUND);          //We haven't read the file yet so no properties were ever found.
        propertiesLoadingStatus.put(propertyServerIP, propertyLoadingState.NOT_FOUND);
    }
    /**
     * What name implies.
     * @return String containing port for the server. Guaranteed to be parsable to an integer.
     */
    public int getUsedPort() {
        return Integer.parseInt(usedPort);
    }
    /**Returns IP of the server. Default value is localhost.*/
    public String getServerIP() { return usedServerIP;}

    /**Port that will be used by the server. By default the defaultPort.  */
    private String usedPort = defaultPort;
    private String usedServerIP = defaultServerIP;

    /** Interface for lambda creation. Allows for easier change of property reading methods. */
    interface ReadPropertyValueMethod
    {
        /**
         * Recognizes type of property and reads its value.
         * @param property Raw property read from file.
         */
        void readValue(String property);
    }

    /**
     * Finds the separator index (colon) in the passed property.
     * @param propertyString String containing the property.
     * @return Position of sign right after the separator sign in the property or negative number if separator was not found.
     */
    private int readSeparatorIndex(String propertyString)
    {
        int colonIndex = propertyString.indexOf(separatorChar); //Each property contains a colon after which the value shall be inserted.
        if(colonIndex >= 0) {                         //If not found the colon - report that you are unable to read property.
            return colonIndex + 1;
        }
        else
        {
            return colonIndex;
        }

    }

    /**
     * Assures that white characters are removed and returns only the value from the whole property line.
     * @param property Property which the value shall be extracted from.
     * @param afterSeparatorIndex Separator index (default colon) which wil be used to find the value.
     * @return String containing read value of the property.
     */
    private String preparePropertyValue(String property, int afterSeparatorIndex)
    {
        String propertyValue = property.substring(afterSeparatorIndex); //Get the value itself
        propertyValue = ParseModifyString.removeWhiteChars(propertyValue);          //Get rid of any white characters

        return propertyValue;
    }

    /**
     * Modifies the content of propertiesLoadingStatus. Later on the program can determine what, if even so, has gone wrong during loading properties.
     * @param propertyIdentifier Name of the property that has just been parsed.
     * @param loadingState Result of parsing.
     */
    private void setPropertyLoadingStatus(String propertyIdentifier, propertyLoadingState loadingState)
    {
        propertiesLoadingStatus.replace(propertyIdentifier, loadingState);
    }
    /**
     * Reads server-port value.
     */
    private ReadPropertyValueMethod readServerPort = new ReadPropertyValueMethod() {
        /**
         * Reads passed property value.
         * @param property Property from file (single line).
         */
        @Override
        public void readValue(String property) {
            property = property.toLowerCase();              //Make the property ignore letter size

            if(property.contains(propertyServerPort))
            {
                int colonIndex = readSeparatorIndex(property);     //Each property contains a colon after which the value shall be inserted.
                if(colonIndex < 0) {                        //If not found the colon - report that you are unable to read property.
                    setPropertyLoadingStatus(propertyServerPort, propertyLoadingState.INCORRECT);
                    return;
                }

                String propertyValue = preparePropertyValue(property, colonIndex);

                if(ParseModifyString.tryStringToInt(propertyValue))    //Since we read the server port, check if it is correct
                {
                    usedPort = propertyValue;
                    setPropertyLoadingStatus(propertyServerPort, propertyLoadingState.FOUND);                  //The port can be correctly loaded.
                }
                else
                {
                    setPropertyLoadingStatus(propertyServerPort, propertyLoadingState.INCORRECT);              //The port was incorrectly written.
                }
            }
        }
    };
    /**
     * Reads server IP value. The ip is not checked.
     */
    private ReadPropertyValueMethod readServerIP = new ReadPropertyValueMethod() {
        @Override
        public void readValue(String property) {
            property = property.toLowerCase();

            if(property.contains(propertyServerIP))
            {
                int colonIndex = readSeparatorIndex(property);     //Each property contains a colon after which the value shall be inserted.
                if(colonIndex < 0) {                        //If not found the colon - report that you are unable to read property.
                    setPropertyLoadingStatus(propertyServerIP, propertyLoadingState.INCORRECT);
                    return;
                }

                usedServerIP = preparePropertyValue(property, colonIndex);

                setPropertyLoadingStatus(propertyServerIP, propertyLoadingState.FOUND);
            }
        }
    };
    /**
     * Manages configuration loading. After calling this method data can be retrieved.
     * @return Loading result. Check {@link ConfigLoader.loadingResult}
     */
    public loadingResult loadConfiguration()
    {
        loadingResult result = readFile();

        if(result == loadingResult.FILE_NOT_FOUND)
        {
            restoreFile();
        }

        return result;
    }

    /**
     * Called when properties file is not present. Restores it and fills it with default values.
     */
    private void restoreFile()
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, true));

            writer.write(propertyServerPort + separatorChar + defaultPort + '\n');
            writer.write(propertyServerIP + separatorChar + defaultServerIP + '\n');

            writer.close();
        }
        catch(IOException ex)
        {
            System.out.print("Could not recreate the properties file!");
        }
    }

    /**
     * Sends passed property to all available parsers.
     * @param property Property to parse.
     */
    private void chkSingleProperty(String property)
    {
        for(ReadPropertyValueMethod parser : propertyParsers)
        {
            parser.readValue(property);
        }
    }

    /**
     * Summarizes the properties loading result by checking states in the propertiesLoadingStatus map.
     * @return FULL if everything was loaded correctly. PARTIAL if something went wrong during loading but still some parts could be loaded.
     *  FILE_NOT_FOUND if the properties file could not be found. Most likely was recreated, though. NOT_PERFORMED if file was present but the program couldn't get anything useful from it.
     */
    private loadingResult summarizeLoadingResult()
    {
        loadingResult result = loadingResult.FULL;          //We assume at the beginning that everything went smoothly.

        int correctReadsCount = 0;
        for(propertyLoadingState state : propertiesLoadingStatus.values())
        {
            if(state.equals(propertyLoadingState.FOUND))    //Count amount of correctly loaded properties.
            {
                correctReadsCount++;
            }
        }

        if(correctReadsCount != propertiesLoadingStatus.size()) {
            result = loadingResult.PARTIAL;                  //Not all values were loading correctly.
        }
        if(correctReadsCount <=0)
        {
            result = loadingResult.NOT_PERFORMED;            //None of the values were loaded correctly.
        }

        return result;
    }
    /**
     * Reads the properties file.
     * @return State describing loading result.
     */
    private loadingResult readFile()
    {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(configFile));
            String rawProperty = reader.readLine();                         //Attempt to get first line from the properties file.
                                                                            //Try to parse each property (each in separate row) with every parser available.
            while(rawProperty != null) {                                    //Readline returns NULL if no more rows can be read from a file.

                chkSingleProperty(rawProperty);

                rawProperty = reader.readLine();                             //Get next line from the file.
            }

            reader.close();
        }
        catch(FileNotFoundException ex)
        {
            return loadingResult.FILE_NOT_FOUND;    //File was not found. Could not perform loading.
        }
        catch(IOException ex)
        {
            return loadingResult.PARTIAL;           //An exception occurred during loading - data is not guaranteed to be fully loaded.
                                                    //Some parts of it may remain default.
        }

        return summarizeLoadingResult();            //At the end count how many properties were loaded correctly and return proper state.
    }


}

