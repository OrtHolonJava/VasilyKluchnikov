package pieces.chessPieces;

import boardgame.BoardPosition;
import directionVectors.ChessDirectionVector;
import enums.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public class Rook extends ChessPiece
{
    private static Collection<ChessDirectionVector> chessDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            add(new ChessDirectionVector(true,1, 0));
            add(new ChessDirectionVector(true,0, 1));
            add(new ChessDirectionVector(true,-1, 0));
            add(new ChessDirectionVector(true,0, -1));
        }
    };

    private static BoardPosition whiteKingSideCastlePosition, blackKingSideCastlePosition,
            whiteQueenSideCastlePosition, blackQueenSideCastlePosition;

    static
    {
        whiteKingSideCastlePosition = new BoardPosition(7, 5);
        whiteQueenSideCastlePosition = new BoardPosition(7, 3);
        blackKingSideCastlePosition = new BoardPosition(0, 5);
        blackQueenSideCastlePosition = new BoardPosition(0, 3);
    }

    public Rook(Player player)
    {
        super(player);
    }

    @Override
    public Collection<ChessDirectionVector> getDirectionVectors()
    {
        return chessDirectionVectors;
    }

    public static BoardPosition getWhiteKingSideCastlePosition()
    {
        return whiteKingSideCastlePosition;
    }

    public static BoardPosition getBlackKingSideCastlePosition()
    {
        return blackKingSideCastlePosition;
    }

    public static BoardPosition getWhiteQueenSideCastlePosition()
    {
        return whiteQueenSideCastlePosition;
    }

    public static BoardPosition getBlackQueenSideCastlePosition()
    {
        return blackQueenSideCastlePosition;
    }
}
