package utils;

import boardgame.Chess;
import enums.Player;
import gameStates.ChessState;
import pieces.chessPieces.*;

/**
 * Contains static functions related to the chess board
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
                if(ch == 'P')
                {
                    board[x][y] = new Pawn(player);
                }
                else if(ch == 'N')
                {
                    board[x][y] = new Knight(player);
                }
                else if(ch == 'B')
                {
                    board[x][y] = new Bishop(player);
                }
                else if(ch == 'R')
                {
                    board[x][y] = new Rook(player);
                }
                else if(ch == 'Q')
                {
                    board[x][y] = new Queen(player);
                }
                else if(ch == 'K')
                {
                    board[x][y] = new King(player);
                }
                else
                {
                    throw new IllegalArgumentException("Invalid FEN");
                }

                y = (y + 1) % board.length;
            }
        }
        return new ChessState<ChessPiece>(board, Player.WHITE);
    }
}
