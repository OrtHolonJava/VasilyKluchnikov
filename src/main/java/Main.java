import boardgame.Chess;
import bots.ChessBot;
import enums.Player;
import exceptions.BoardGameException;
import gameStates.ChessState;

/**
 * Created by divided on 15.11.2017.
 */
public class Main
{
    public static void main(String[] args)
    {
        Chess<ChessState> chess = new Chess<ChessState>();
        try
        {
            chess.playBotGame(new ChessBot(), 3, Player.WHITE);
        }
        catch (BoardGameException e)
        {
            e.printStackTrace();
        }
    }
}
