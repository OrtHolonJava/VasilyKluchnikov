package exceptions.boardExceptions;

import exceptions.ChessException;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class BoardException extends ChessException
{
    public BoardException(String message)
    {
        super(message);
    }
}
