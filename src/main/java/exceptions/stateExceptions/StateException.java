package exceptions.stateExceptions;

import exceptions.ChessException;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class StateException extends ChessException
{
    private static final long serialVersionUID = -5363707938159935972L;

    public StateException(String message)
    {
        super(message);
    }
}
