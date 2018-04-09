package exceptions;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class ChessException extends BoardGameException
{
    public ChessException(String message)
    {
        super(message);
    }
}
