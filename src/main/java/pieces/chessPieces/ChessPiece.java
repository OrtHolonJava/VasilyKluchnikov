package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import enums.Player;
import pieces.Piece;

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
