package bots;

import gameStates.BoardGameState;
import pieces.Piece;

/**
 * Created by divided on 19.03.2018.
 */
public interface BoardGameBot
{
    double evaluate(BoardGameState<Piece> state);
    BoardGameState findBestNextState(BoardGameState<Piece> state, int depth);
}
