package exceptions.boardExceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class WinCheckException extends BoardException
{
    private static final long serialVersionUID = 1463736626593926227L;

    public WinCheckException(String message)
    {
        super(message);
    }
}
