package boardgame;

import bots.BoardGameBot;
import bots.ChessBot;
import enums.Player;
import exceptions.stateExceptions.KingNotFoundException;
import gameStates.BoardGameState;
import gameStates.ChessState;
import pieces.chessPieces.ChessPiece;
import utils.ChessBoardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by divided on 19.03.2018.
 */
public class Chess<T extends ChessState> extends BoardGame
{
    public static final int boardSize = 8;

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    @Override
    protected BoardGameState getStartingState()
    {
        return ChessBoardUtils.getStateFromFen(STARTING_FEN);
    }

    public Chess()
    {
        currentState = getStartingState();
        previousStates = new ArrayList<ChessState>();
    }

    @Override
    protected GameResult getGameResult()
    {
        List<T> possibleStates = currentState.getAllPossibleStates();
        if (possibleStates.isEmpty())
        {
            try
            {
                if(((T)currentState).kingIsUnderCheck(currentState.getPlayerToMove()))
                {
                    Player winner = (currentState.getPlayerToMove() == Player.WHITE) ? Player.BLACK : Player.WHITE;
                    return new GameResult(true, winner);
                }
                else
                {
                    return new GameResult(true, null);
                }
            }
            catch (KingNotFoundException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        else
        {
            return new GameResult(false, null);
        }
    }

    @Override
    public void playGame()
    {
        GameResult gameResult = getGameResult();
        while(!(gameResult.isGameFinished()))
        {
            ChessBoardUtils.displayBoard((ChessPiece[][]) currentState.getBoard());
            previousStates.add(currentState);
            currentState = getNewStateFromPlayer();
            turnCount++;
            gameResult = getGameResult();
        }

        if(gameResult.getWinner() != null)
        {
            System.out.println("Winner: " + gameResult.getWinner().name());
        }
        else
        {
            System.out.println("Draw!");
        }
    }

    @Override
    public void playBotGame(BoardGameBot bot, Player player)
    {
        GameResult gameResult = getGameResult();
        ChessBot chessBot = new ChessBot();
        while(!(gameResult.isGameFinished()))
        {
            ChessBoardUtils.displayBoard((ChessPiece[][]) currentState.getBoard());
            previousStates.add(currentState);
            if(currentState.getPlayerToMove() == player)
            {
                currentState = getNewStateFromPlayer();
            }
            else
            {
                currentState = bot.findBestNextState(currentState, 5);
            }

            turnCount++;
            gameResult = getGameResult();
        }

        if(gameResult.getWinner() != null)
        {
            System.out.println("Winner: " + gameResult.getWinner().name());
        }
        else
        {
            System.out.println("Draw!");
        }
    }


    @Override
    protected T getNewStateFromPlayer()
    {
        System.out.println("Enter position of piece to move: ");
        BoardPosition piecePosition = getPositionFromPlayer();
        List<BoardPosition> possiblePositions = ((T)currentState).getPossiblePositionsForPiece(piecePosition);

        for(BoardPosition possiblePosition : possiblePositions)
        {
            ChessState newState = ((T)currentState).getStateAfterMove(piecePosition, possiblePosition);
            try
            {
                if(newState.kingIsUnderCheck(((T)currentState).getPlayerToMove()))
                {
                    possiblePositions.remove(possiblePosition);
                }
            }
            catch (KingNotFoundException e)
            {
                e.printStackTrace();
            }
        }


        System.out.println("Possible options are: ");
        for(int i = 1; i <= possiblePositions.size(); i++)
        {
            System.out.println(i + ".  " +
                    possiblePositions.get(i - 1).getX() + "," + possiblePositions.get(i - 1).getY());
        }
        System.out.println("Enter the option: ");
        int choice = scanner.nextInt();

        return (T) ((T) currentState).getStateAfterMove(piecePosition, possiblePositions.get(choice - 1));
    }


    static Scanner scanner = new Scanner(System.in);
    private static BoardPosition getPositionFromPlayer()
    {
        System.out.print("x: ");
        int x = scanner.nextInt();
        System.out.print("y: ");
        int y = scanner.nextInt();
        return new BoardPosition(x, y);
    }
}
