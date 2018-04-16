package ui;

import boardgame.GameResult;
import enums.Player;
import pieces.chessPieces.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by divided on 13.04.2018.
 */
public class ChessBoardUI
{
    private static Map<String, Character> pieceNameToCharMap;

    static
    {
        pieceNameToCharMap = new HashMap<String, Character>();
        pieceNameToCharMap.put(Pawn.class.getSimpleName(), 'P');
        pieceNameToCharMap.put(Knight.class.getSimpleName(), 'N');
        pieceNameToCharMap.put(Bishop.class.getSimpleName(), 'B');
        pieceNameToCharMap.put(Rook.class.getSimpleName(), 'R');
        pieceNameToCharMap.put(Queen.class.getSimpleName(), 'Q');
        pieceNameToCharMap.put(King.class.getSimpleName(), 'K');
    }

    /*
        Textually outputs the current chess board
    */
    public static void displayBoard(ChessPiece[][] board)
    {
        System.out.println();
        for (int j = 0; j < board[0].length; j++)
        {
            System.out.printf("%5d", j);
        }
        System.out.println();

        for(int x = 0; x < board.length; x++)
        {
            for(int y = 0; y < board[0].length; y++)
            {
                char charToPrint = getCharRepresentationFromPiece(board[x][y]);
                System.out.printf("%5s", charToPrint);
            }
            System.out.printf("%5d\n", x);
        }
        System.out.println();
    }

    /*
       Outputs the winner of the game accordingly to the game result
    */
    public static void outputWinner(GameResult gameResult)
    {
        if(gameResult.getWinner() != null)
        {
            System.out.println("Winner: " + gameResult.getWinner().name());
        }
        else
        {
            System.out.println("Draw!");
        }
    }

    /*
            Gets char representation for the chess piece
    */
    private static char getCharRepresentationFromPiece(ChessPiece piece)
    {
        if(piece == null)
            return '-';

        Character ch = pieceNameToCharMap.get(piece.getClass().getSimpleName());

        if(piece.getPlayer() == Player.BLACK)
        {
            ch = Character.toLowerCase(ch);
        }
        return ch;
    }
}
