package boardgame;

import bots.BoardGameBot;
import enums.Player;
import exceptions.BoardGameException;
import exceptions.boardExceptions.InvalidInputPositionException;
import exceptions.stateExceptions.InvalidStateChangeException;
import gameStates.BoardGameState;
import gameStates.ChessState;
import gameStates.StateResult;
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
        StateResult stateResult = getCurrentState().getStateResult();
        while(!(stateResult.isGameFinished()))
        {
            BoardUI.displayBoard((ChessPiece[][]) getCurrentState().getBoard());
            getPreviousStates().add(getCurrentState());
            setCurrentState(getNewStateFromPlayer());
            setTurnCount(getTurnCount() + 1);
            stateResult = getCurrentState().getStateResult();;
        }

        BoardUI.displayBoard((ChessPiece[][]) getCurrentState().getBoard());
        BoardUI.outputWinner(stateResult);
    }

    /*
        Starts a player vs. bot game and manages it
        Manages turn order, input and stops the game when its finished
     */
    @Override
    public void playBotGame(BoardGameBot bot, int searchDepth, Player player) throws BoardGameException
    {
        StateResult stateResult = getCurrentState().getStateResult();
        while(!(stateResult.isGameFinished()))
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
            stateResult = getCurrentState().getStateResult();
        }

        BoardUI.displayBoard((ChessPiece[][]) getCurrentState().getBoard());
        BoardUI.outputWinner(stateResult);
    }

    @Override
    protected BoardGameState getStartingState()
    {
        return ChessBoardUtils.getStateFromFen(STARTING_FEN);
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
            throw new InvalidStateChangeException("Invalid change to the state");
        }

        return newState;
    }
}
