package exceptions.boardExceptions;

import exceptions.boardExceptions.BoardException;

/**
 * Created by divided on 21.03.2018.
 */
public class WinCheckException extends BoardException
{
    public WinCheckException(String message)
    {
        super(message);
    }
}
