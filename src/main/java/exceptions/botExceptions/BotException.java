package exceptions.botExceptions;

import exceptions.ChessException;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class BotException extends ChessException
{
    public BotException(String message)
    {
        super(message);
    }
}
