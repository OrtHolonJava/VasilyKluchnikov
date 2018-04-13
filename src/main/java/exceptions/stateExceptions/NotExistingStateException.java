package exceptions.stateExceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class NotExistingStateException extends IllegalStateException
{
    private static final long serialVersionUID = 961395356077032195L;

    public NotExistingStateException(String message)
    {
        super(message);
    }
}
