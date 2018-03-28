package exceptions.stateExceptions;

import exceptions.ChessException;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class StateException extends ChessException
{
    public StateException(String message)
    {
        super(message);
    }
}
