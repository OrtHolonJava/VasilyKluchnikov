package bots;

import enums.Player;
import exceptions.BoardGameException;
import exceptions.botExceptions.BotEvaluateException;
import exceptions.botExceptions.BotMoveSearchException;
import gameStates.BoardGameState;
import pieces.Piece;

import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public abstract class BoardGameBot
{
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
    public MinimaxResult minimax(BoardGameState state, int depth, double alpha, double beta) throws BoardGameException
    {
        List<BoardGameState> allPossibleStates = (List<BoardGameState>)state.getAllPossibleStates();

        if(depth == 0)
        {
            double evaluateValue = evaluate(state);
            if(state.getPlayerToMove() == Player.BLACK)
            {
                evaluateValue *= -1;
            }
            return new MinimaxResult(evaluateValue, null);
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        BoardGameState bestState = null;
        for(BoardGameState nextState : allPossibleStates)
        {
            MinimaxResult result = minimax(nextState, depth - 1, -1 * beta, -1 * alpha);
            double value = -1 * result.getBestValue();
            if(value > bestValue)
            {
                bestValue = value;
                bestState = nextState;
            }

            if(value > alpha)
            {
                alpha = value;
            }
            if(alpha >= beta)
            {
                break;
            }
        }

        return new MinimaxResult(bestValue, bestState);
    }
}
