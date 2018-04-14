package exceptions.boardExceptions;

/**
 * Created by divided on 14.04.2018.
 */
public class InvalidInputPositionException extends InvalidPositionException
{
    private static final long serialVersionUID = -7304063261385557300L;

    public InvalidInputPositionException(String message)
    {
        super(message);
    }
}
