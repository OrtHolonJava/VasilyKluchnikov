package boardgame;

import enums.Player;

/**
 * Created by divided on 16.04.2018.
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
