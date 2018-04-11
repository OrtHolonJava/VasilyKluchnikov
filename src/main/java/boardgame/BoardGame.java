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

    /*
        Starts a player vs. player game and manages it
        Manages turn order, input and stops the game when its finished
     */
    public abstract void playGame() throws BoardGameException;

    /*
        Starts a player vs. bot game and manages it
        Manages turn order, input and stops the game when its finished
     */
    public abstract void playBotGame(BoardGameBot bot, int searchDepth, Player player) throws BoardGameException;

    protected abstract T getStartingState();

    /*
        Gets the game result for the ongoing game
        Game result includes an indication if the game is finished, and the winning player (null if one doesn't exist)
     */
    protected abstract GameResult getGameResult() throws BoardGameException;

    /*
        Gets new state from the player
     */
    protected abstract T getNewStateFromPlayer() throws BoardGameException;

    public T getCurrentState()
    {
        return currentState;
    }

    public Collection<T> getPreviousStates()
    {
        return previousStates;
    }

    public int getTurnCount()
    {
        return turnCount;
    }

    public void setCurrentState(T currentState)
    {
        this.currentState = currentState;
    }

    public void setPreviousStates(Collection<T> previousStates)
    {
        this.previousStates = previousStates;
    }

    public void setTurnCount(int turnCount)
    {
        this.turnCount = turnCount;
    }
}
