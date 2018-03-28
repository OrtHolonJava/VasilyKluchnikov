package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import enums.Player;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public abstract class ChessPiece<T extends ChessDirectionVector> extends Piece
{
    public ChessPiece(Player player)
    {
        super(player);
    }
}
