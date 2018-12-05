package pl.polsl.server;

import pl.polsl.utility.dataCheck.ParseModifyString;

import java.io.*;

/** Loads configuration data for the server from a .properties file.
 * @author Karol Kozuch Group 4 Section 8
 * @version 1.0
 */
public class ConfigLoader {
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
    private String configFile = "serverConfig.properties";
    /**Default port used by the server.*/
    private final String defaultPort = "8090";

    /**
     * What name implies.
     * @return String containing port for the server. Guaranteed to be parsable to an integer.
     */
    public String getUsedPort() {
        return usedPort;
    }

    /**Port that will be used by the server. By default the defaultPort.  */
    private String usedPort = defaultPort;

    /** Interface for lambda creation. Allows for easier change of property reading methods. */
    interface ReadPropertyValueMethod
    {
        boolean readValue(String property);
    }

    /**
     * Reads server-port value.
     */
    private ReadPropertyValueMethod readServerPort = new ReadPropertyValueMethod() {
        /**
         * Reads passed property value.
         * @param property Property from file (single line).
         * @return TRUE if property was correctly read. FALSE otherwise.
         */
        @Override
        public boolean readValue(String property) {
            property = property.toLowerCase();              //Make the property ignore letter size
            String serverPort = "server-port";

            if(property.contains(serverPort))
            {
                int colonIndex = property.indexOf(':');     //Each property contains a colon after which the value shall be inserted.
                if(colonIndex < 0) {                        //If not found the colon - report that you are unable to read property.
                    return false;
                }

                String propertyValue = property.substring(colonIndex+1); //Get the value itself
                propertyValue = ParseModifyString.removeWhiteChars(propertyValue);          //Get rid of any white characters

                if(ParseModifyString.tryStringToInt(propertyValue))    //Since we read the server port, check if it is correct
                {
                    usedPort = propertyValue;
                    return true;
                }
                return false;
            }
            else
            {
                return false;
            }
        }
    };

    /**
     * Manages configuration loading. After calling this method data can be retrieved.
     * @return Loading result. Check {@link ConfigLoader.loadingResult}
     */
    public loadingResult LoadConfiguration()
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

            writer.write("server-port:" + defaultPort);

            writer.close();
        }
        catch(IOException ex)
        {
            System.out.print("Could not recreate the properties file!");
        }
    }

    /**
     * Reads the properties file.
     * @return State describing loading result.
     */
    private loadingResult readFile()
    {
        ReadPropertyValueMethod readPropertyValue;//Slot for methods that will be used in reading property values.
        BufferedReader reader = null;
        loadingResult result = loadingResult.NOT_PERFORMED;

        try {
            reader = new BufferedReader(new FileReader(configFile));

            readPropertyValue = readServerPort;
            if(!readPropertyValue.readValue(reader.readLine()))
            {
                result = loadingResult.PARTIAL;     //There was a problem while reading server port. Default value will be used.
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

        if(result != loadingResult.PARTIAL) {
            result = loadingResult.FULL;            //If there were no problems during reading data - set the result to FULL.
        }

        return result;
    }


}
