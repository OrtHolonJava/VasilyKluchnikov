package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import enums.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public class Knight extends ChessPiece
{
    private static ArrayList<ChessDirectionVector> chessDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            add(new ChessDirectionVector(false,2, 1));
            add(new ChessDirectionVector(false,2, -1));
            add(new ChessDirectionVector(false,1, -1));
            add(new ChessDirectionVector(false,-1, -1));
        }
    };

    public Knight(Player player)
    {
        super(player);
    }

    @Override
    public List<ChessDirectionVector> getDirectionVectors()
    {
        return chessDirectionVectors;
    }
}
