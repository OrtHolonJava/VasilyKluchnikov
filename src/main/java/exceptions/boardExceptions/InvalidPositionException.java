package exceptions.boardExceptions;

/**
 * Created by divided on 09.04.2018.
 */
public class InvalidPositionException extends BoardException
{
    private static final long serialVersionUID = 8306942664976858624L;

    public InvalidPositionException(String message)
    {
        super(message);
    }
}
