package boardgame;

import exceptions.BoardGameException;
import gameStates.BoardGameState;
import java.util.ArrayList;
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
        setCurrentState(getStartingState());
        currentState = getStartingState();
        setPreviousStates(new ArrayList<T>());
        setTurnCount(0);
    }

    /*
        Given a new state, makes a move by setting the current state to the new one
     */
    public void makeMove(T newState)
    {
        getPreviousStates().add(currentState);
        setCurrentState(newState);
        setTurnCount(getTurnCount() + 1);
    }

    public abstract GameResult getGameResult() throws BoardGameException;

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

    protected abstract T getStartingState();

    private void setCurrentState(T currentState)
    {
        this.currentState = currentState;
    }

    private void setPreviousStates(Collection<T> previousStates)
    {
        this.previousStates = previousStates;
    }

    private void setTurnCount(int turnCount)
    {
        this.turnCount = turnCount;
    }
}
