package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import enums.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public class Queen extends ChessPiece
{

    private static Collection<ChessDirectionVector> chessDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            add(new ChessDirectionVector(true,1, 0));
            add(new ChessDirectionVector(true,0, 1));
            add(new ChessDirectionVector(true,-1, 0));
            add(new ChessDirectionVector(true,0, -1));
            add(new ChessDirectionVector(true,1, 1));
            add(new ChessDirectionVector(true,-1, 1));
            add(new ChessDirectionVector(true,1, -1));
            add(new ChessDirectionVector(true,-1, -1));
        }
    };

    public Queen(Player player)
    {
        super(player);
    }

    @Override
    public Collection<ChessDirectionVector> getDirectionVectors()
    {
        return chessDirectionVectors;
    }
}
