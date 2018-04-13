package exceptions.botExceptions;

import exceptions.ChessException;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class BotException extends ChessException
{
    private static final long serialVersionUID = 6944293598657086183L;

    public BotException(String message)
    {
        super(message);
    }
}
