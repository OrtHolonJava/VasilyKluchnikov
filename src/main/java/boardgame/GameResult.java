package boardgame;

import enums.Player;

/**
 * Created by divided on 21.03.2018.
 */
public class GameResult
{
    private boolean gameFinished;
    private Player winner;

    public GameResult(boolean gameFinished, Player winner)
    {
        this.gameFinished = gameFinished;
        this.winner = winner;
    }

    public boolean isGameFinished()
    {
        return gameFinished;
    }

    public Player getWinner()
    {
        return winner;
    }
}
