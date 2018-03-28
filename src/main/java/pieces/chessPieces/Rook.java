package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import enums.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public class Rook extends ChessPiece
{
    private static ArrayList<ChessDirectionVector> chessDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            add(new ChessDirectionVector(true,1, 0));
            add(new ChessDirectionVector(true,0, 1));
            add(new ChessDirectionVector(true,-1, 0));
            add(new ChessDirectionVector(true,0, -1));
        }
    };

    public Rook(Player player)
    {
        super(player);
    }

    @Override
    public List<ChessDirectionVector> getDirectionVectors()
    {
        return chessDirectionVectors;
    }
}
