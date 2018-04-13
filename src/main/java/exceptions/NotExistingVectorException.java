package exceptions;

/**
 * Created by divided on 21.03.2018.
 */
public class NotExistingVectorException extends ChessException
{
    private static final long serialVersionUID = -1541494350928791188L;

    public NotExistingVectorException(String message)
    {
        super(message);
    }
}
