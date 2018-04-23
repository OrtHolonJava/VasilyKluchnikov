package enums;

import java.util.Random;

/**
 * Enum which represents a player
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

    /*
        Gets a random player
     */
    public static Player getRandomPlayer()
    {
        Random random = new Random();
        int randomResult = random.nextInt(2);
        if(randomResult == 1)
        {
            return Player.WHITE;
        }
        else
        {
            return Player.BLACK;
        }
    }
}
