package gameStates;

import boardgame.BoardPosition;
import directionVectors.ChessDirectionVector;
import enums.Player;
import exceptions.BoardGameException;
import exceptions.boardExceptions.InvalidPositionException;
import exceptions.boardExceptions.KingNotFoundException;
import pieces.chessPieces.ChessPiece;
import pieces.chessPieces.King;
import pieces.chessPieces.Pawn;
import pieces.chessPieces.Queen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public class ChessState<T extends ChessPiece> extends BoardGameState<T>
{

    public ChessState(T[][] board, Player playerToMove)
    {
        super(board, playerToMove);
    }

    public ChessState(ChessState<T> state)
    {
        super(state);
    }

    @Override
    public List<BoardGameState<T>> getAllPossibleStates() throws BoardGameException
    {
        ArrayList<BoardGameState<T>> possibleStates = new ArrayList<BoardGameState<T>>();
        
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                T piece = board[i][j];
                if (piece != null && piece.getPlayer() == getPlayerToMove())
                {
                    BoardPosition piecePosition = new BoardPosition(i, j);
                    List<BoardPosition> possiblePositions = getPossiblePositionsForPiece(piecePosition);
                    for(BoardPosition possiblePosition : possiblePositions)
                    {
                        ChessState<T> newState = getStateAfterMove(piecePosition, possiblePosition);
                        try
                        {
                            if(!(newState.kingIsUnderCheck(getPlayerToMove())))
                            {
                                possibleStates.add(newState);
                            }
                        }
                        catch (KingNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return possibleStates;
    }

    public List<BoardPosition> getPossiblePositionsForPiece(BoardPosition piecePosition) throws InvalidPositionException
    {
        int x = piecePosition.getX(), y = piecePosition.getY();

        if(!isPositionOnBoard(piecePosition) || board[x][y] == null)
        {
            throw new InvalidPositionException("Invalid BoardPosition");
        }

        T piece = board[x][y];
        if (piece instanceof Pawn)
        {
            return getPossiblePositionsForPawn(piecePosition);
        }

        List<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();
        for (ChessDirectionVector directionVector : (List<ChessDirectionVector>)piece.getDirectionVectors())
        {
            BoardPosition positionChange = getPositionChangeFromDirectionVector(directionVector, piece.getPlayer());

            BoardPosition newPosition = new BoardPosition(piecePosition);
            newPosition.addToPosition(positionChange);
            if(isPositionLegalToMoveOn(newPosition, piece.getPlayer()))
            {
                possiblePositions.add(newPosition);
                if(directionVector.isRepeating() && board[newPosition.getX()][newPosition.getY()] == null)
                {
                    newPosition = new BoardPosition(newPosition);
                    newPosition.addToPosition(positionChange);
                    while(isPositionLegalToMoveOn(newPosition, piece.getPlayer()))
                    {
                        possiblePositions.add(newPosition);
                        if(board[newPosition.getX()][newPosition.getY()] != null) break; // In case a piece is captured
                        newPosition = new BoardPosition(newPosition);
                        newPosition.addToPosition(positionChange);
                    }
                }
            }
        }
        
        return possiblePositions;
    }

    public ChessState<T> getStateAfterMove(BoardPosition oldPosition, BoardPosition newPosition)
    {
        return getStateAfterMove(oldPosition.getX(), oldPosition.getY(), newPosition.getX(), newPosition.getY());
    }

    public ChessState<T> getStateAfterMove(int oldX, int oldY, int newX, int newY)
    {
        ChessState<T> newState = new ChessState<T>(this);
        T pieceToMove = newState.board[oldX][oldY];
        newState.playerToMove = (pieceToMove.getPlayer() == Player.WHITE) ? Player.BLACK : Player.WHITE;
        newState.board[newX][newY] = pieceToMove;
        newState.board[oldX][oldY] = null;
        if(pieceNeedsToBePromoted(newX, pieceToMove))
        {
            newState.board[newX][newY] = (T)new Queen(pieceToMove.getPlayer());
        }
        return newState;
    }

    public boolean kingIsUnderCheck(Player kingsPlayer) throws KingNotFoundException, InvalidPositionException
    {
        for(int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[0].length; j++)
            {
                T piece = board[i][j];
                if(piece != null && piece.getPlayer() == kingsPlayer && piece instanceof King)
                {
                    return isPositionUnderAttack(new BoardPosition(i, j), kingsPlayer);
                }
            }
        }

        throw new KingNotFoundException("King not found on board");
    }

    private List<BoardPosition> getPossiblePositionsForPawn(BoardPosition pawnPosition) throws InvalidPositionException
    {
        int x = pawnPosition.getX(), y = pawnPosition.getY();

        if(!isPositionOnBoard(pawnPosition) || board[x][y] == null || !(board[x][y] instanceof Pawn))
        {
            throw new InvalidPositionException("Invalid position for the pawn");
        }

        Pawn pawn = (Pawn)board[x][y];
        List<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();
        for (ChessDirectionVector directionVector : pawn.getDirectionVectors())
        {
            BoardPosition positionChange = getPositionChangeFromDirectionVector(directionVector, pawn.getPlayer());
            BoardPosition newPosition = new BoardPosition(pawnPosition);
            newPosition.addToPosition(positionChange);
            if(isPositionOnBoard(newPosition) && board[newPosition.getX()][newPosition.getY()] == null)
            {
                possiblePositions.add(newPosition);
                if(!hasPawnMoved(pawnPosition))
                {
                    newPosition = new BoardPosition(newPosition);
                    newPosition.addToPosition(positionChange);
                    if(board[newPosition.getX()][newPosition.getY()] == null)
                    {
                        possiblePositions.add(newPosition);
                    }
                }
            }

        }

        for(ChessDirectionVector directionVector : pawn.getAttackDirectionVectors())
        {
            BoardPosition positionChange = getPositionChangeFromDirectionVector(directionVector, pawn.getPlayer());
            BoardPosition newPosition = new BoardPosition(pawnPosition);
            newPosition.addToPosition(positionChange);
            if(isPositionLegalToMoveOn(newPosition, pawn.getPlayer()) &&
                    board[newPosition.getX()][newPosition.getY()] != null)
            {
                possiblePositions.add(newPosition);
            }
        }

        return possiblePositions;
    }

    private BoardPosition getPositionChangeFromDirectionVector(ChessDirectionVector directionVector, Player player)
    {
        List<Integer> directions = (List<Integer>) directionVector.getDirections();
        BoardPosition positionChange = new BoardPosition(directions.get(0), directions.get(1));
        if (player == Player.WHITE)
        {
            positionChange.multiplyPosition(-1);
        }

        return positionChange;
    }

    private boolean isPositionUnderAttack(BoardPosition position, Player playerUnderAttack) throws InvalidPositionException
    {
        if(!(isPositionOnBoard(position)))
        {
            throw new IndexOutOfBoundsException("Position is out of bounds");
        }

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                T piece = board[i][j];
                if (piece != null && piece.getPlayer() != playerUnderAttack)
                {
                    BoardPosition piecePosition = new BoardPosition(i, j);
                    List<BoardPosition> possiblePositions = getPossiblePositionsForPiece(piecePosition);
                    if(possiblePositions.contains(position))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean pieceNeedsToBePromoted(int x, ChessPiece chessPiece)
    {
        return chessPiece instanceof Pawn && (x == 0 || x == board.length - 1);
    }

    private boolean isPositionOnBoard(BoardPosition position)
    {
        return isPositionOnBoard(position.getX(), position.getY());
    }
    
    private boolean isPositionOnBoard(int x, int y)
    {
        return !(x < 0 || y < 0 || x >= board.length || y >= board[0].length);
    }

    private boolean isPositionLegalToMoveOn(BoardPosition position, Player movingPlayer)
    {
        return isPositionLegalToMoveOn(position.getX(), position.getY(), movingPlayer);
    }

    private boolean isPositionLegalToMoveOn(int x, int y, Player movingPlayer)
    {
        return isPositionOnBoard(x,y) &&
                (board[x][y] == null || board[x][y].getPlayer() != movingPlayer);
    }

    private boolean hasPawnMoved(BoardPosition pawnPosition) throws InvalidPositionException
    {
        int x = pawnPosition.getX(), y = pawnPosition.getY();

        if(!isPositionOnBoard(pawnPosition) || board[x][y] == null || !(board[x][y] instanceof Pawn))
        {
            throw new InvalidPositionException("Invalid board position for the pawn");
        }

        if(board[x][y].getPlayer() == Player.WHITE)
        {
            return x != (board.length - 2);
        }
        else
        {
            return x != 1;
        }
    }
}
