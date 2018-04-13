package exceptions;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class ChessException extends BoardGameException
{
    private static final long serialVersionUID = -1568284507788569123L;

    public ChessException(String message)
    {
        super(message);
    }
}
