package boardgame;

import bots.BoardGameBot;
import enums.Player;
import exceptions.BoardGameException;
import exceptions.stateExceptions.InvalidStateChangeException;
import gameStates.BoardGameState;
import gameStates.ChessState;
import pieces.chessPieces.ChessPiece;
import ui.BoardUI;
import ui.GameInputGetter;
import utils.ChessBoardUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public class Chess<T extends ChessState> extends BoardGame
{
    public static final int boardSize = 8;

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public Chess()
    {
        super();
        setPreviousStates(new ArrayList<ChessState>());
    }

    /*
            Starts a player vs. player game and manages it
            Manages turn order, input and stops the game when its finished
    */
    @Override
    public void playGame() throws BoardGameException
    {
        GameResult gameResult = getGameResult();
        while(!(gameResult.isGameFinished()))
        {
            BoardUI.displayBoard((ChessPiece[][]) getCurrentState().getBoard());
            getPreviousStates().add(getCurrentState());
            setCurrentState(getNewStateFromPlayer());
            setTurnCount(getTurnCount() + 1);
            gameResult = getGameResult();
        }

        BoardUI.outputWinner(gameResult);
    }

    /*
        Starts a player vs. bot game and manages it
        Manages turn order, input and stops the game when its finished
     */
    @Override
    public void playBotGame(BoardGameBot bot, int searchDepth, Player player) throws BoardGameException
    {
        GameResult gameResult = getGameResult();
        while(!(gameResult.isGameFinished()))
        {
            BoardUI.displayBoard((ChessPiece[][]) getCurrentState().getBoard());
            getPreviousStates().add(getCurrentState());
            if(getCurrentState().getPlayerToMove() == player)
            {
                setCurrentState(getNewStateFromPlayer());
            }
            else
            {
                setCurrentState(bot.findBestNextState(getCurrentState(), searchDepth));
            }

            setTurnCount(getTurnCount() + 1);
            gameResult = getGameResult();
        }

        BoardUI.outputWinner(gameResult);
    }

    @Override
    protected BoardGameState getStartingState()
    {
        return ChessBoardUtils.getStateFromFen(STARTING_FEN);
    }

    /*
        Gets the game result for the ongoing game
        Game result includes an indication if the game is finished, and the winning player (null if one doesn't exist)
     */
    @Override
    protected GameResult getGameResult() throws BoardGameException
    {
        Collection<T> possibleStates = currentState.getAllPossibleStates();
        if (possibleStates.isEmpty())
        {
            if(((T)getCurrentState()).kingIsUnderCheck(getCurrentState().getPlayerToMove()))
            {
                Player winner;
                if(getCurrentState().getPlayerToMove() == Player.WHITE)
                {
                    winner = Player.BLACK;
                }
                else
                {
                    winner = Player.WHITE;
                }
                return new GameResult(true, winner);
            }
            else
            {
                return new GameResult(true, null);
            }
        }
        else
        {
            return new GameResult(false, null);
        }
    }

    /*
        Gets new state from the player
     */
    @Override
    protected T getNewStateFromPlayer() throws InvalidStateChangeException
    {
        GameInputGetter inputGetter = new GameInputGetter();
        T newState;
        try
        {
            newState = (T) inputGetter.getChessStateInputFromUser((T) getCurrentState());
        }
        catch (BoardGameException e)
        {
            e.printStackTrace();
            throw new InvalidStateChangeException("Error getting state input from player");
        }
        return newState;
    }
}
