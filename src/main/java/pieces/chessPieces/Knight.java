package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import enums.Player;

import java.util.ArrayList;
import java.util.Collection;
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

            add(new ChessDirectionVector(false,-2, 1));
            add(new ChessDirectionVector(false,-2, -1));

            add(new ChessDirectionVector(false,1, 2));
            add(new ChessDirectionVector(false,-1, 2));

            add(new ChessDirectionVector(false,1, -2));
            add(new ChessDirectionVector(false,-1, -2));
        }
    };

    public Knight(Player player)
    {
        super(player);
    }

    @Override
    public Collection<ChessDirectionVector> getDirectionVectors()
    {
        return chessDirectionVectors;
    }
}
