package pl.polsl.exceptions;
/**Indicates that a function has been called for calculations but wasn't previously set.*/
public class NoFunctionAssignedException extends Throwable {
    public NoFunctionAssignedException(String message)
    {
        super(message);
    }

}
