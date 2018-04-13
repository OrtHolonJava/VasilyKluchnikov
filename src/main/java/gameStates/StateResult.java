package gameStates;

import enums.Player;

/**
 * Created by divided on 13.04.2018.
 */
public class StateResult
{
    private boolean gameFinished;
    private Player winner;

    public StateResult(boolean gameFinished, Player winner)
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
