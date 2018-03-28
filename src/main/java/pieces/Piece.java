package pieces;

import directionVectors.DirectionVector;
import enums.Player;

import java.util.Collection;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public abstract class Piece<T extends DirectionVector>
{
    private Player player;

    public Piece(Player player)
    {
        this.player = player;
    }

    public Piece(Piece piece)
    {
        this.player = piece.player;
    }

    public abstract List<T> getDirectionVectors();

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }
}
