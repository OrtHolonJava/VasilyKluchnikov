package utils;

import boardgame.Chess;
import enums.Player;
import gameStates.ChessState;
import pieces.chessPieces.*;

import java.util.Properties;

/**
 * Created by divided on 22.03.2018.
 */
public class ChessBoardUtils
{
    /*
        Converts FEN code (the board part of it) to a chess state
        FEN code explanation - https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     */
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
                Player player;
                if(Character.isUpperCase(ch))
                {
                    player = Player.WHITE;
                }
                else
                {
                    player = Player.BLACK;
                }
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

    /*
        Textually outputs the current chess board
     */
    public static void displayBoard(ChessPiece[][] board)
    {
        System.out.println();
        for (int j = 0; j < board[0].length; j++)
        {
            System.out.print(" " + j + " ");
        }
        System.out.println();

        for(int x = 0; x < board.length; x++)
        {
            for(int y = 0; y < board[0].length; y++)
            {
                String charToPrint = getStringRepresentationFromPiece(board[x][y]);
                System.out.print(" " + charToPrint + " ");
            }
            System.out.println("  " + x);
        }
        System.out.println();
    }


    private static Properties pieceNameToStringMap;

    static
    {
        pieceNameToStringMap = new Properties();
        pieceNameToStringMap.put(Pawn.class.getSimpleName(), "P");
        pieceNameToStringMap.put(Knight.class.getSimpleName(), "N");
        pieceNameToStringMap.put(Bishop.class.getSimpleName(), "B");
        pieceNameToStringMap.put(Rook.class.getSimpleName(), "R");
        pieceNameToStringMap.put(Queen.class.getSimpleName(), "Q");
        pieceNameToStringMap.put(King.class.getSimpleName(), "K");
    }

    /*
            Gets string representation for the chess piece
    */
    private static String getStringRepresentationFromPiece(ChessPiece piece)
    {
        if(piece == null)
            return "-";

        String st = pieceNameToStringMap.getProperty(piece.getClass().getSimpleName());

        if(piece.getPlayer() == Player.BLACK)
        {
            st = st.toLowerCase();
        }
        return st;
    }
}
