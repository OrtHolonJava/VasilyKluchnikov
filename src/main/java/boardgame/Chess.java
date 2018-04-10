package boardgame;

import bots.BoardGameBot;
import enums.Player;
import exceptions.BoardGameException;
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

    public Chess()
    {
        super();
        previousStates = new ArrayList<ChessState>();
    }

    @Override
    public void playGame() throws BoardGameException
    {
        GameResult gameResult = getGameResult();
        while(!(gameResult.isGameFinished()))
        {
            ChessBoardUtils.displayBoard((ChessPiece[][]) currentState.getBoard());
            previousStates.add(currentState);
            try
            {
                currentState = getNewStateFromPlayer();
            }
            catch (BoardGameException e)
            {
                System.out.println("Error getting new state from player!");
                e.printStackTrace();
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
    public void playBotGame(BoardGameBot bot, int searchDepth, Player player) throws BoardGameException
    {
        GameResult gameResult = getGameResult();
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
                currentState = bot.findBestNextState(currentState, searchDepth);
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
    protected BoardGameState getStartingState()
    {
        return ChessBoardUtils.getStateFromFen(STARTING_FEN);
    }

    @Override
    protected GameResult getGameResult() throws BoardGameException
    {
        List<T> possibleStates = currentState.getAllPossibleStates();
        if (possibleStates.isEmpty())
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
        else
        {
            return new GameResult(false, null);
        }
    }


    @Override
    protected T getNewStateFromPlayer() throws BoardGameException
    {
        System.out.println("Enter position of piece to move: ");
        BoardPosition piecePosition = getPositionFromPlayer();
        List<BoardPosition> possiblePositions = ((T)currentState).getPossiblePositionsForPiece(piecePosition);

        for(int i = 0; i < possiblePositions.size(); i++)
        {
            BoardPosition possiblePosition = possiblePositions.get(i);
            ChessState newState = ((T)currentState).getStateAfterMove(piecePosition, possiblePosition);
            if(newState.kingIsUnderCheck(((T)currentState).getPlayerToMove()))
            {
                possiblePositions.remove(possiblePosition);
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


    // Console input will be replaced with a gui
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
