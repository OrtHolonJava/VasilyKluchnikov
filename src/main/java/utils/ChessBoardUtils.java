package utils;

import boardgame.Chess;
import enums.Player;
import gameStates.ChessState;
import pieces.chessPieces.*;

/**
 * Created by divided on 22.03.2018.
 */
public class ChessBoardUtils
{
    public static ChessState<ChessPiece> getStateFromFen(String fenString)
    {
        ChessPiece[][] board = new ChessPiece[Chess.boardSize][Chess.boardSize];
        int x = 0, y = 0;
        for(int i = 0; i < fenString.length(); i++)
        {
            char ch = fenString.charAt(i);
            if(ch >= '1' && ch <= '8')
            {
                y = (y + ch - '0') % board.length;
            }
            else if (ch == '/')
            {
                x++;
            }
            else
            {
                Player player = Character.isUpperCase(ch) ? Player.WHITE : Player.BLACK;
                ch = Character.toUpperCase(ch);
                switch(ch)
                {
                    case 'P':
                        board[x][y] = new Pawn(player);
                        break;
                    case 'N':
                        board[x][y] = new Knight(player);
                        break;
                    case 'B':
                        board[x][y] = new Bishop(player);
                        break;
                    case 'R':
                        board[x][y] = new Rook(player);
                        break;
                    case 'Q':
                        board[x][y] = new Queen(player);
                        break;
                    case 'K':
                        board[x][y] = new King(player);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid FEN");
                }

                y = (y + 1) % board.length;
            }
        }
        return new ChessState<ChessPiece>(board, Player.WHITE);
    }

    public static void displayBoard(ChessPiece[][] board)
    {
        System.out.println();
        for (int j = 0; j < board[0].length; j++)
        {
            System.out.print(" " + j + " ");
        }
        System.out.println();

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                char charToPrint = getCharRepresentationFromPiece(board[i][j]);
                System.out.print(" " + charToPrint + " ");
            }
            System.out.println("  " + i);
        }
        System.out.println();
    }

    private static char getCharRepresentationFromPiece(ChessPiece piece)
    {
        char ch = ' ';
        if(piece == null)
            return '-';
        else if (piece instanceof Pawn)
            ch = 'P';
        else if (piece instanceof Knight)
            ch = 'N';
        else if (piece instanceof Bishop)
            ch = 'B';
        else if (piece instanceof Rook)
            ch = 'R';
        else if (piece instanceof Queen)
            ch = 'Q';
        else if (piece instanceof King)
            ch = 'K';

        if(piece.getPlayer() == Player.BLACK)
        {
            ch = Character.toLowerCase(ch);
        }
        return ch;
    }
}
