package ui;

import boardgame.BoardPosition;
import exceptions.BoardGameException;
import gameStates.ChessState;

import java.util.List;
import java.util.Scanner;

/**
 * Created by divided on 10.04.2018.
 */
public class InputGetter
{
    /*
        Returns next chess state using player input
     */
    public ChessState getChessStateInputFromUser(ChessState currentState) throws BoardGameException
    {
        System.out.println("Enter position of piece to move: ");
        BoardPosition piecePosition = getPositionFromPlayer();
        List<BoardPosition> possiblePositions = currentState.getPossiblePositionsForPiece(piecePosition);

        for(int i = 0; i < possiblePositions.size(); i++)
        {
            BoardPosition possiblePosition = possiblePositions.get(i);
            ChessState newState = currentState.getStateAfterMove(piecePosition, possiblePosition);
            if(newState.kingIsUnderCheck(currentState.getPlayerToMove()))
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

        return currentState.getStateAfterMove(piecePosition, possiblePositions.get(choice - 1));
    }

    static Scanner scanner = new Scanner(System.in);

    /*
        Returns a position given by the player input
     */
    private static BoardPosition getPositionFromPlayer()
    {
        System.out.print("x: ");
        int x = scanner.nextInt();
        System.out.print("y: ");
        int y = scanner.nextInt();
        return new BoardPosition(x, y);
    }
}
