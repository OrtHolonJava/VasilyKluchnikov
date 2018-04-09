package bots;

import gameStates.BoardGameState;

/**
 * Created by divided on 26.03.2018.
 */
public class MinimaxResult
{
    private double bestValue;
    private BoardGameState bestState;

    public MinimaxResult(double bestValue, BoardGameState bestState)
    {
        this.bestValue = bestValue;
        this.bestState = bestState;
    }

    public double getBestValue()
    {
        return bestValue;
    }

    public BoardGameState getBestState()
    {
        return bestState;
    }
}
