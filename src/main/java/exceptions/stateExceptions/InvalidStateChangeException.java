package exceptions.stateExceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class InvalidStateChangeException extends StateException
{
    public InvalidStateChangeException(String message)
    {
        super(message);
    }
}
