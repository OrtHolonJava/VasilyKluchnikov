package exceptions.stateExceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class InvalidStateChangeException extends StateException
{
    private static final long serialVersionUID = 1623405985647349769L;

    public InvalidStateChangeException(String message)
    {
        super(message);
    }
}
