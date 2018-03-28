package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import directionVectors.DirectionVector;
import enums.Player;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public class Bishop extends ChessPiece
{
    private static ArrayList<ChessDirectionVector> chessDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            add(new ChessDirectionVector(true,1, 1));
            add(new ChessDirectionVector(true,-1, 1));
            add(new ChessDirectionVector(true,1, -1));
            add(new ChessDirectionVector(true,-1, -1));
        }
    };

    public Bishop(Player player)
    {
        super(player);
    }

    @Override
    public List<ChessDirectionVector> getDirectionVectors()
    {
        return chessDirectionVectors;
    }
}
