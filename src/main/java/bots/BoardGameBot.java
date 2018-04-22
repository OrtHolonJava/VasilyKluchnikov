package bots;

import enums.Player;
import exceptions.BoardGameException;
import exceptions.botExceptions.BotEvaluateException;
import exceptions.botExceptions.BotMoveSearchException;
import gameStates.BoardGameState;
import pieces.Piece;

import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public abstract class BoardGameBot
{
    private BoardGameState lastBestMinimaxState;

    /*
        Gives an evaluation, a numerical score, to a state
        The evaluation is positive for one player, negative for another
     */
    public abstract double evaluate(BoardGameState<Piece> state) throws BotEvaluateException;

    /*
        Given a state, finds the best state using the evaluation method, up to the given depth
     */
    public abstract BoardGameState findBestNextState(BoardGameState<Piece> state, int depth) throws BotMoveSearchException;

    /*
        Minimax algorithm used to find the best next state
        Uses alpha-beta pruning optimization
     */
    public double minimax(BoardGameState state, int depth, double alpha, double beta) throws BoardGameException
    {
        Collection<BoardGameState> allPossibleStates = state.getAllPossibleStates();
        if(depth == 0 || allPossibleStates.isEmpty())
        {
            setLastBestMinimaxState(state);
            double evaluateScore = evaluate(state);

            if(state.getPlayerToMove() == Player.BLACK)
            {
                evaluateScore *= -1;
            }
            return evaluateScore;
        }

        double bestStateValue = Double.NEGATIVE_INFINITY;
        BoardGameState bestState = null;
        for(BoardGameState nextState : allPossibleStates)
        {
            double currentStateValue = -1 * minimax(nextState, depth - 1, -1 * beta, -1 * alpha);
            if(currentStateValue > bestStateValue)
            {
                bestStateValue = currentStateValue;
                bestState = nextState;
            }

            if(currentStateValue > alpha)
            {
                alpha = currentStateValue;
            }
            if(alpha >= beta)
            {
                break;
            }
        }

        setLastBestMinimaxState(bestState);
        return bestStateValue;
    }

    public BoardGameState getLastBestMinimaxState()
    {
        return lastBestMinimaxState;
    }

    public void setLastBestMinimaxState(BoardGameState lastBestMinimaxState)
    {
        this.lastBestMinimaxState = lastBestMinimaxState;
    }
}
