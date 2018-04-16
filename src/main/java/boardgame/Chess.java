package boardgame;

import enums.Player;
import exceptions.BoardGameException;
import gameStates.BoardGameState;
import gameStates.ChessState;
import utils.ChessBoardUtils;

import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public class Chess<T extends ChessState> extends BoardGame
{
    public static final int boardSize = 8;

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public Chess()
    {
        super();
    }

    /*
        Gets the result of the current game, if the game is finished and it's winner (if exists)
     */
    @Override
    public GameResult getGameResult() throws BoardGameException
    {
            Collection<T> possibleStates = currentState.getAllPossibleStates();
            if (possibleStates.isEmpty())
            {
                if(((T)getCurrentState()).kingIsUnderCheck(getCurrentState().getPlayerToMove()))
                {
                    Player winner;
                    if(getCurrentState().getPlayerToMove() == Player.WHITE)
                    {
                        winner = Player.BLACK;
                    }
                    else
                    {
                        winner = Player.WHITE;
                    }
                    return new GameResult(true, winner);
                }
                else
                {
                    return new GameResult(true, null);
                }
            }
            else
            {
                return new GameResult(false, null);
            }
        }

    @Override
    protected BoardGameState getStartingState()
    {
        return ChessBoardUtils.getStateFromFen(STARTING_FEN);
    }
}
