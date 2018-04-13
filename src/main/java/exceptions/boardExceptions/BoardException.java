package exceptions.boardExceptions;

import exceptions.ChessException;

/**
 * Created by divided on 21.03.2018.
 */
public abstract class BoardException extends ChessException
{
    private static final long serialVersionUID = 7871420273065601906L;

    public BoardException(String message)
    {
        super(message);
    }
}
