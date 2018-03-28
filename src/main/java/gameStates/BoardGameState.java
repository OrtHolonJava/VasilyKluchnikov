package gameStates;

import boardgame.Chess;
import enums.Player;
import pieces.Piece;
import pieces.chessPieces.ChessPiece;

import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public abstract class BoardGameState<T extends Piece>
{
    protected T[][] board;
    Player playerToMove;

    public BoardGameState(T[][] board, Player playerToMove)
    {
        this.board = board;
        this.playerToMove = playerToMove;
    }

    public BoardGameState(BoardGameState<T> state)
    {
        this.playerToMove = state.playerToMove;
        board = (T[][]) new ChessPiece[Chess.boardSize][Chess.boardSize];
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                board[i][j] = state.board[i][j]; // TODO: 23.03.2018 The piece is not actually cloned. HasMoved therefore is unreliable. 
            }
        }

    }

    public abstract List<BoardGameState<T>> getAllPossibleStates();

    public T[][] getBoard()
    {
        return board;
    }

    public Player getPlayerToMove()
    {
        return playerToMove;
    }
}
