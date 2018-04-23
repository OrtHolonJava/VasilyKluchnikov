package boardgame;

import enums.Player;

/**
 * Game result, contains whether the game is finished, and the winner of it
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
