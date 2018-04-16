import bots.ChessBot;
import enums.Player;
import exceptions.BoardGameException;
import ui.ChessUI;

/**
 * Created by divided on 15.11.2017.
 */
public class Main
{
    public static void main(String[] args)
    {
        ChessUI chessUI = new ChessUI();
        try
        {
            chessUI.playBotGame(new ChessBot(), 3, Player.WHITE);
        }
        catch (BoardGameException e)
        {
            e.printStackTrace();
        }
    }
}
