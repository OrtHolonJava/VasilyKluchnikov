package bots;

import enums.Player;
import exceptions.BoardGameException;
import exceptions.botExceptions.BotEvaluateException;
import exceptions.botExceptions.BotMoveSearchException;
import gameStates.BoardGameState;
import gameStates.ChessState;
import pieces.Piece;
import pieces.chessPieces.*;

import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public class ChessBot implements BoardGameBot
{
    @Override
    public double evaluate(BoardGameState<Piece> state) throws BotEvaluateException
    {
        double whiteScore = 0, blackScore = 0;
        Piece[][] board = state.getBoard();
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                Piece piece = board[i][j];

                if (piece != null)
                {
                    double score = getPieceValue(piece);
                    if(piece.getPlayer() == Player.WHITE)
                    {
                        whiteScore += score;
                    }
                    else
                    {
                        blackScore += score;
                    }
                }
            }
        }

        return (whiteScore - blackScore);
    }



    @Override
    public BoardGameState findBestNextState(BoardGameState state, int depth) throws BotMoveSearchException
    {
        MinimaxResult minimaxResult;
        try
        {
            minimaxResult = minimax(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        catch (BoardGameException exception)
        {
            throw new BotMoveSearchException("Failed to evaluate the position");
        }
        return minimaxResult.getBestState();
    }

    private double getPieceValue(Piece piece) throws BotEvaluateException
    {
        if (piece instanceof Pawn)
        {
            return 1;
        }
        else if (piece instanceof Knight)
        {
            return 3.2;
        }
        else if (piece instanceof Bishop)
        {
            return 3.3;
        }
        else if (piece instanceof Rook)
        {
            return 5;
        }
        else if (piece instanceof Queen)
        {
            return 9;
        }
        else if (piece instanceof King)
        {
            return 500;
        }
        throw new BotEvaluateException("Trying to evaluate an invalid piece");
    }

    private MinimaxResult minimax(BoardGameState state, int depth, double alpha, double beta) throws BoardGameException
    {
        List<BoardGameState> allPossibleStates = (List<BoardGameState>)state.getAllPossibleStates();

        if(allPossibleStates.isEmpty())
        {
            double value = 0;
            if(((ChessState)state).kingIsUnderCheck(state.getPlayerToMove()))
            {
                value = (state.getPlayerToMove() == Player.WHITE)
                        ? 500 : -500;
            }

            return new MinimaxResult(value, null);
        }
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

            alpha = (alpha > value) ? alpha : value;
            if(alpha >= beta)
            {
                break;
            }
        }

        return new MinimaxResult(bestValue, bestState);
    }
}
