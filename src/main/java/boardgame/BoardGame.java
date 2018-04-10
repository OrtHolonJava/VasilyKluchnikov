package boardgame;

import bots.BoardGameBot;
import enums.Player;
import exceptions.BoardGameException;
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

    public abstract void playGame() throws BoardGameException;

    public abstract void playBotGame(BoardGameBot bot, int searchDepth, Player player) throws BoardGameException;

    protected abstract T getStartingState();

    protected abstract GameResult getGameResult() throws BoardGameException;

    protected abstract T getNewStateFromPlayer() throws BoardGameException;
}
