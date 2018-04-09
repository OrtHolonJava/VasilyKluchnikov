package bots;

import exceptions.botExceptions.BotEvaluateException;
import exceptions.botExceptions.BotMoveSearchException;
import gameStates.BoardGameState;
import pieces.Piece;

/**
 * Created by divided on 19.03.2018.
 */
public interface BoardGameBot
{
    double evaluate(BoardGameState<Piece> state) throws BotEvaluateException;
    BoardGameState findBestNextState(BoardGameState<Piece> state, int depth) throws BotMoveSearchException;
}
