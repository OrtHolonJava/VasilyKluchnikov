package ui;

import boardgame.BoardPosition;
import exceptions.BoardGameException;
import exceptions.boardExceptions.InvalidInputPositionException;
import gameStates.ChessState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Created by divided on 10.04.2018.
 */
public class GameInputGetter
{
    private static Scanner scanner = new Scanner(System.in);

    /*
        Returns next chess state using player input
     */
    public static ChessState getChessStateInputFromUser(ChessState currentState) throws BoardGameException
    {
        System.out.println("Enter position of piece to move: ");
        BoardPosition piecePosition;
        try
        {
            piecePosition = getPositionFromPlayer(currentState);
        }
        catch (InvalidInputPositionException e)
        {
            System.out.println("Error in choosing a position: " + e.getMessage() +".\nTry inputting again.\n");
            return getChessStateInputFromUser(currentState);
        }

        Collection<BoardPosition> uncheckedPossiblePositions = currentState.getPossiblePositionsForPiece(piecePosition);
        List<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();

        for(BoardPosition uncheckedPosition : uncheckedPossiblePositions)
        {
            ChessState newState = currentState.getStateAfterMove(piecePosition, uncheckedPosition);
            if(currentState.isMoveLegal(newState, piecePosition, uncheckedPosition))
            {
                possiblePositions.add(uncheckedPosition);
            }
        }

        int choice;
        try
        {
            choice = getUsersChoiceOfPosition(possiblePositions);
        }
        catch (InvalidInputPositionException e)
        {
            System.out.println("Error in choosing a position: " + e.getMessage() +".\nTry inputting again.\n");
            return getChessStateInputFromUser(currentState);
        }
        return currentState.getStateAfterMove(piecePosition, possiblePositions.get(choice - 1));
    }

    /*
        Gets the user choice for a positions, from all possible new positions
     */
    private static int getUsersChoiceOfPosition(List<BoardPosition> possiblePositions) throws InvalidInputPositionException
    {
        if(possiblePositions.size() == 0)
        {
            throw new InvalidInputPositionException("The piece inputted has no moves");
        }

        System.out.println("Possible options are: ");
        for(int i = 1; i <= possiblePositions.size(); i++)
        {
            System.out.println(i + ".  " +
                    possiblePositions.get(i - 1).getX() + "," + possiblePositions.get(i - 1).getY());
        }
        System.out.println("Enter the option: ");
        int choice = scanner.nextInt();

        if(choice < 1 || choice > possiblePositions.size())
        {
            throw new InvalidInputPositionException("Incorrect input for the option");
        }

        return choice;
    }

    /*
        Returns a position given by the player input
     */
    private static BoardPosition getPositionFromPlayer(ChessState currentState) throws InvalidInputPositionException
    {
        System.out.print("x: ");
        int x = scanner.nextInt();
        System.out.print("y: ");
        int y = scanner.nextInt();
        BoardPosition inputtedPosition = new BoardPosition(x, y);
        if(!currentState.isValidPiecePosition(inputtedPosition))
        {
            throw new InvalidInputPositionException("The inputted position is not a valid piece position");
        }

        return inputtedPosition;
    }
}
