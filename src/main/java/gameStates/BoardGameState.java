package gameStates;

import enums.Player;
import exceptions.BoardGameException;
import pieces.Piece;

import java.util.Collection;

/**
 * Created by divided on 19.03.2018.
 */
public abstract class BoardGameState<T extends Piece>
{
    protected T[][] board;
    private Player playerToMove;

    public BoardGameState(T[][] board, Player playerToMove)
    {
        this.board = board;
        this.playerToMove = playerToMove;
    }

    public BoardGameState(BoardGameState<T> state)
    {
        this.playerToMove = state.playerToMove;

        board = state.board.clone();
        for(int i = 0; i < board.length; i++)
        {
            board[i] = state.board[i].clone(); // Note: The pieces aren't deep cloned
        }
    }

    /*
        Returns all states that are possible after this state
     */
    public abstract Collection<BoardGameState<T>> getAllPossibleStates() throws BoardGameException;

    public T[][] getBoard()
    {
        return board;
    }

    public Player getPlayerToMove()
    {
        return playerToMove;
    }

    public void setPlayerToMove(Player playerToMove)
    {
        this.playerToMove = playerToMove;
    }
}
