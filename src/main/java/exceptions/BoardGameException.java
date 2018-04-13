package exceptions;

/**
 * Created by divided on 09.04.2018.
 */
public abstract class BoardGameException extends Exception
{
    private static final long serialVersionUID = 6095137442648123852L;

    public BoardGameException(String message)
    {
        super(message);
    }
}
