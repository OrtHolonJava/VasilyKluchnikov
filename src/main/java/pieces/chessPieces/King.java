package pieces.chessPieces;

import boardgame.BoardPosition;
import directionVectors.ChessDirectionVector;
import enums.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public class King extends ChessPiece
{
    private static Collection<ChessDirectionVector> chessDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            // Clockwise moves by 1 square forward, starting from up
            add(new ChessDirectionVector(false,1, 0));
            add(new ChessDirectionVector(false,1, 1));
            add(new ChessDirectionVector(false,0, 1));
            add(new ChessDirectionVector(false, -1, 1));
            add(new ChessDirectionVector(false,-1, 0));
            add(new ChessDirectionVector(false,-1, -1));
            add(new ChessDirectionVector(false, 0, -1));
            add(new ChessDirectionVector(false,1, -1));

        }
    };

    private static BoardPosition whiteKingSideCastlePosition, blackKingSideCastlePosition,
            whiteQueenSideCastlePosition, blackQueenSideCastlePosition;

    static
    {
        whiteKingSideCastlePosition = new BoardPosition(7, 6);
        whiteQueenSideCastlePosition = new BoardPosition(7, 2);
        blackKingSideCastlePosition = new BoardPosition(0, 6);
        blackQueenSideCastlePosition = new BoardPosition(0, 2);
    }

    public King(Player player)
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
