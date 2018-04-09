package exceptions;

/**
 * Created by divided on 09.04.2018.
 */
public abstract class BoardGameException extends Exception
{
    public BoardGameException(String message)
    {
        super(message);
    }
}
