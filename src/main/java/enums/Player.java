package enums;

/**
 * Created by divided on 19.03.2018.
 */
public enum Player
{
    WHITE,
    BLACK;

    /*
        Returns the opposite player for the given player
     */
    public static Player getOppositePlayer(Player player)
    {
        if(player == Player.WHITE)
        {
            return Player.BLACK;
        }
        else
        {
            return Player.WHITE;
        }
    }
}
