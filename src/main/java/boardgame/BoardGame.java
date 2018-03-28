package boardgame;

import bots.BoardGameBot;
import enums.Player;
import gameStates.BoardGameState;

import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public abstract class BoardGame<T extends BoardGameState>
{
    protected T currentState;
    protected Collection<T> previousStates;
    protected int turnCount;

    public BoardGame()
    {
        currentState = getStartingState();
        previousStates = null;
        turnCount = 0;
    }

    protected abstract T getStartingState();

    protected abstract GameResult getGameResult();

    public abstract void playGame();

    public abstract void playBotGame(BoardGameBot bot, Player player);

    protected abstract T getNewStateFromPlayer();
}
