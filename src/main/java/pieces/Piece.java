package pieces;

import directionVectors.DirectionVector;
import enums.Player;

import java.util.Collection;

/**
 * A generic piece class, each piece contains its own direction vectors
 * @param <T> The default direction vector
 */
public abstract class Piece<T extends DirectionVector>
{
    private Player player;

    public abstract Collection<T> getDirectionVectors();

    public Piece(Player player)
    {
        this.player = player;
    }

    public Piece(Piece piece)
    {
        this.player = piece.player;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }
}
