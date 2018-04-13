package exceptions.boardExceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class KingNotFoundException extends BoardException
{
    private static final long serialVersionUID = -4545918156379742302L;

    public KingNotFoundException(String message)
    {
        super(message);
    }
}
