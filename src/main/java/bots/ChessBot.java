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
public class ChessBot extends BoardGameBot
{
    /*
        Gives an evaluation, a numerical score, to a state
        The evaluation is positive for one player, negative for another
     */
    @Override
    public double evaluate(BoardGameState<Piece> state) throws BotEvaluateException
    {
        double whiteScore = 0, blackScore = 0;
        Piece[][] board = state.getBoard();
        for(int x = 0; x < board.length; x++)
        {
            for(int y = 0; y < board[0].length; y++)
            {
                Piece piece = board[x][y];

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

    /*
        Given a state, finds the best state using the evaluation method, up to the given depth
     */
    @Override
    public BoardGameState findBestNextState(BoardGameState state, int depth) throws BotMoveSearchException
    {
        MinimaxResult minimaxResult;
        try
        {
            minimaxResult = super.minimax(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        catch (BoardGameException exception)
        {
            throw new BotMoveSearchException("Failed to evaluate the position");
        }
        return minimaxResult.getBestState();
    }

    /*
        Returns a numerical value for a given piece
     */
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
}
