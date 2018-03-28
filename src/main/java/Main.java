import boardgame.Chess;
import bots.ChessBot;
import enums.Player;
import gameStates.ChessState;

/**
 * Created by divided on 15.11.2017.
 */
public class Main
{
    public static void main(String[] args)
    {
        Chess<ChessState> chess = new Chess<ChessState>();
        chess.playBotGame(new ChessBot(), Player.WHITE);
    }
}
