package exceptions.stateExceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class IllegalStateException extends StateException
{
    private static final long serialVersionUID = 8294267768036625997L;

    public IllegalStateException(String message)
    {
        super(message);
    }
}
