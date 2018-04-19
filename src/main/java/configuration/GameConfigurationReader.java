package configuration;

import boardgame.Chess;
import bots.ChessBot;
import enums.Player;

/**
 * Created by divided on 19.04.2018.
 */
public class GameConfigurationReader
{
    public static Chess getChessGame()
    {
        return new Chess();
    }

    public static ChessBot getChessBot()
    {
        return new ChessBot();
    }

    public static int getBotSearchDepth()
    {
        return 3;
    }

    public static Player getBotPlayer()
    {
        return Player.BLACK;
    }
}
