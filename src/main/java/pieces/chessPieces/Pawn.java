package pieces.chessPieces;

import directionVectors.ChessDirectionVector;
import enums.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public class Pawn extends ChessPiece
{
    private boolean hadSpecialTurnLastMove;

    public Pawn(Player player)
    {
        super(player);
    }

    private static ArrayList<ChessDirectionVector> chessDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            add(new ChessDirectionVector(false,1, 0));
        }
    };

    private static ArrayList<ChessDirectionVector> attackDirectionVectors = new ArrayList<ChessDirectionVector>()
    {
        {
            add(new ChessDirectionVector(false,1, 1));
            add(new ChessDirectionVector(false,1, -1));
        }
    };

    @Override
    public Collection<ChessDirectionVector> getDirectionVectors()
    {
        return chessDirectionVectors;
    }

    public List<ChessDirectionVector> getAttackDirectionVectors()
    {
        return attackDirectionVectors;
    }

    public boolean hadSpecialTurnLastMove()
    {
        return hadSpecialTurnLastMove;
    }

    public Pawn setHadSpecialTurnLastMove(boolean hadSpecialTurnLastMove)
    {
        this.hadSpecialTurnLastMove = hadSpecialTurnLastMove;
        return this;
    }
}
