package exceptions.stateExceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class NotExistingStateException extends IllegalStateException
{
    public NotExistingStateException(String message)
    {
        super(message);
    }
}
