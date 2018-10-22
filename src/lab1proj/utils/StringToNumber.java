package lab1proj.utils;

/**Class for parse checks of string to numbers.*/
public class StringToNumber {
    /**Tries to parse the passed string to integer value.
     * @param str The string that will be tested.
     * @return TRUE if it is possible to parse the string to integer, FALSE otherwise.*/
    public static boolean TryStringToInt(String str)
    {
        try {
            int number = Integer.parseInt(str);
        }
        catch(NumberFormatException ex)
        {
            return false;
        }

        return true;
    }
}
