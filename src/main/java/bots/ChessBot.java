package bots;

import enums.Player;
import exceptions.BoardGameException;
import exceptions.botExceptions.BotEvaluateException;
import exceptions.botExceptions.BotMoveSearchException;
import gameStates.BoardGameState;
import pieces.Piece;
import pieces.chessPieces.*;

import java.util.HashMap;
import java.util.Map;

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


    private static final double PAWN_VALUE = 1;
    private static final double KNIGHT_VALUE = 3.2;
    private static final double BISHOP_VALUE = 3.3;
    private static final double ROOK_VALUE = 5;
    private static final double QUEEN_VALUE = 9;
    private static final double KING_VALUE = 500;

    private static Map<String, Double> pieceNameToValueMap;

    static
    {
        pieceNameToValueMap = new HashMap<String, Double>();
        pieceNameToValueMap.put(Pawn.class.getSimpleName(), PAWN_VALUE);
        pieceNameToValueMap.put(Knight.class.getSimpleName(), KNIGHT_VALUE);
        pieceNameToValueMap.put(Bishop.class.getSimpleName(), BISHOP_VALUE);
        pieceNameToValueMap.put(Rook.class.getSimpleName(), ROOK_VALUE);
        pieceNameToValueMap.put(Queen.class.getSimpleName(), QUEEN_VALUE);
        pieceNameToValueMap.put(King.class.getSimpleName(), KING_VALUE);
    }
    /*
        Returns a numerical value for a given piece
     */
    private double getPieceValue(Piece piece) throws BotEvaluateException
    {
        Double value = pieceNameToValueMap.get(piece.getClass().getSimpleName());
        if(value == null)
        {
            throw new BotEvaluateException("Trying to evaluate an invalid piece");
        }

        return value;
    }
}
