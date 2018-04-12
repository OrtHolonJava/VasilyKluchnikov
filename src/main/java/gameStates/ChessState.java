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
import java.util.Collection;
import java.util.List;

/**
 * Created by divided on 19.03.2018.
 */
public class ChessState<T extends ChessPiece> extends BoardGameState<T>
{
    private BoardPosition positionOfDoubleMovedPawnLastTurn;

    public ChessState(T[][] board, Player playerToMove)
    {
        super(board, playerToMove);
    }

    public ChessState(ChessState<T> state)
    {
        super(state);
    }

    /*
       Returns all states that are possible after this state
    */
    @Override
    public Collection<BoardGameState<T>> getAllPossibleStates() throws BoardGameException
    {
        Collection<BoardGameState<T>> possibleStates = new ArrayList<BoardGameState<T>>();
        
        for(int x = 0; x < getBoard().length; x++)
        {
            for(int y = 0; y < getBoard()[0].length; y++)
            {
                T piece = getBoard()[x][y];
                if (piece != null && piece.getPlayer() == getPlayerToMove())
                {
                    BoardPosition piecePosition = new BoardPosition(x, y);
                    Collection<BoardPosition> possiblePositions = getPossiblePositionsForPiece(piecePosition);
                    for(BoardPosition possiblePosition : possiblePositions)
                    {
                        ChessState<T> newState = getStateAfterMove(piecePosition, possiblePosition);
                        if(!(newState.kingIsUnderCheck(getPlayerToMove())))
                        {
                            possibleStates.add(newState);
                        }
                    }
                }
            }
        }

        return possibleStates;
    }

    /*
        Returns all possible board positions for a single piece (including positions that cause checks)
     */
    public Collection<BoardPosition> getPossiblePositionsForPiece(BoardPosition piecePosition) throws InvalidPositionException
    {
        int x = piecePosition.getX(), y = piecePosition.getY();

        if(!isValidPiecePosition(piecePosition))
        {
            throw new InvalidPositionException("Invalid position for the piece");
        }

        T piece = getPieceByPosition(piecePosition);
        if (piece instanceof Pawn)
        {
            return getPossiblePositionsForPawn(piecePosition);
        }

        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();
        for (ChessDirectionVector directionVector : (Collection<ChessDirectionVector>)piece.getDirectionVectors())
        {
            possiblePositions.addAll(getPossiblePositionsForDirection(directionVector, piecePosition));
        }
        
        return possiblePositions;
    }

    /*
        Gets the new state after making a move on the current state
     */
    public ChessState<T> getStateAfterMove(BoardPosition oldPosition, BoardPosition newPosition)
    {
        ChessState<T> newState = new ChessState<T>(this);
        T pieceToMove = newState.getPieceByPosition(oldPosition);
        if(pieceToMove.getPlayer() == Player.WHITE)
        {
            newState.setPlayerToMove(Player.BLACK);
        }
        else
        {
            newState.setPlayerToMove(Player.WHITE);
        }

        changeNewStateBasedOnSpecialPawnMoves(newState, oldPosition, newPosition);

        newState.setPieceByPosition(pieceToMove, newPosition);
        newState.setPieceByPosition(null , oldPosition);

        if(pieceNeedsToBePromoted(newPosition.getX(), pieceToMove))
        {
            T promotedPiece = (T)new Queen(pieceToMove.getPlayer());
            newState.setPieceByPosition(promotedPiece, newPosition);
        }
        return newState;
    }

    /*
        Checks if the king of the given player is under a check
     */
    public boolean kingIsUnderCheck(Player kingsPlayer) throws KingNotFoundException, InvalidPositionException
    {
        for(int x = 0; x < getBoard().length; x++)
        {
            for (int y = 0; y < getBoard()[0].length; y++)
            {
                T piece = getBoard()[x][y];
                if(piece != null && piece.getPlayer() == kingsPlayer && piece instanceof King)
                {
                    return isPositionUnderAttack(new BoardPosition(x, y), kingsPlayer);
                }
            }
        }

        throw new KingNotFoundException("King not found on board");
    }

    /*
       Changes the new state based on special pawn moves (en-passant and double move)
    */
    private ChessState<T> changeNewStateBasedOnSpecialPawnMoves(ChessState<T> newState, BoardPosition oldPosition,
                                                                BoardPosition newPosition)
    {
        if(isEnPassantMove(oldPosition, newPosition))
        {
            newState.setPieceByPosition(null, getPositionOfDoubleMovedPawnLastTurn());
        }
        else if(isDoublePawnMove(oldPosition, newPosition))
        {
            newState.setPositionOfDoubleMovedPawnLastTurn(newPosition);
        }
        else
        {
            newState.setPositionOfDoubleMovedPawnLastTurn(null);
        }
        return newState;
    }

    /*
        Gets all possible positions for the pawn on the given position (including moves that cause a check)
     */
    private Collection<BoardPosition> getPossiblePositionsForPawn(BoardPosition pawnPosition) throws InvalidPositionException
    {
        if(!isValidPawnPosition(pawnPosition))
        {
            throw new InvalidPositionException("Invalid position for the pawn");
        }

        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();

        possiblePositions.addAll(getPawnRegularPositions(pawnPosition));
        possiblePositions.addAll(getPawnAttackPositions(pawnPosition));
        possiblePositions.addAll(getEnPassantPositions(pawnPosition));
        return possiblePositions;
    }

    /*
       Gets all regular positions (including double move) for the pawn on the given position
    */
    private Collection<BoardPosition> getPawnRegularPositions(BoardPosition pawnPosition) throws InvalidPositionException
    {
        if(!isValidPawnPosition(pawnPosition))
        {
            throw new InvalidPositionException("Invalid position for the pawn");
        }

        Pawn pawn = (Pawn)getPieceByPosition(pawnPosition);
        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();

        for (ChessDirectionVector directionVector : pawn.getDirectionVectors())
        {
            for(BoardPosition newPosition : getPossiblePositionsForDirection(directionVector, pawnPosition))
            {
                if(getPieceByPosition(newPosition) == null)
                {
                    possiblePositions.add(newPosition);

                    if(!hasPawnMoved(pawnPosition))
                    {
                        BoardPosition positionChange = new BoardPosition
                                (newPosition.getX() - pawnPosition.getX(), newPosition.getY() - pawnPosition.getY());
                        BoardPosition doubleMovePosition = new BoardPosition(newPosition);
                        doubleMovePosition.addToPosition(positionChange);

                        if(isPositionOnBoard(doubleMovePosition) && getPieceByPosition(doubleMovePosition) == null)
                        {
                            possiblePositions.add(doubleMovePosition);
                        }
                    }
                }
            }
        }

        return possiblePositions;
    }

    /*
        Gets all attacking positions for the pawn on the given position
     */
    private Collection<BoardPosition> getPawnAttackPositions(BoardPosition pawnPosition) throws InvalidPositionException
    {
        if(!isValidPawnPosition(pawnPosition))
        {
            throw new InvalidPositionException("Invalid position for the pawn");
        }

        Pawn pawn = (Pawn)getPieceByPosition(pawnPosition);
        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();

        for(ChessDirectionVector directionVector : pawn.getAttackDirectionVectors())
        {
            for(BoardPosition newPosition : getPossiblePositionsForDirection(directionVector, pawnPosition))
            {
                if(getPieceByPosition(newPosition) != null)
                {
                    possiblePositions.add(newPosition);
                }
            }
        }
        return possiblePositions;
    }

    /*
        Gets all en-passant positions for the pawn on the given position
     */
    private Collection<BoardPosition> getEnPassantPositions(BoardPosition pawnPosition) throws InvalidPositionException
    {
        if(!isValidPawnPosition(pawnPosition))
        {
            throw new InvalidPositionException("Invalid position for the pawn");
        }

        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();

        if(getPositionOfDoubleMovedPawnLastTurn() != null)
        {
            int doubleMovedX = getPositionOfDoubleMovedPawnLastTurn().getX(),
                    doubleMovedY = getPositionOfDoubleMovedPawnLastTurn().getY();
            BoardPosition newPossiblePosition = new BoardPosition(getPositionOfDoubleMovedPawnLastTurn());

            if(getPieceByPosition(getPositionOfDoubleMovedPawnLastTurn()).getPlayer() == Player.WHITE)
            {
                newPossiblePosition.addToPosition(1, 0);
            }
            else
            {
                newPossiblePosition.addToPosition(-1, 0);
            }
            BoardPosition leftValidPawnPosition = new BoardPosition(doubleMovedX, doubleMovedY - 1),
                    rightValidPawnPosition = new BoardPosition(doubleMovedX, doubleMovedY + 1);

            if ((isPositionOnBoard(leftValidPawnPosition) && leftValidPawnPosition.equals(pawnPosition))
                    || (isPositionOnBoard(rightValidPawnPosition) && rightValidPawnPosition.equals(pawnPosition)))
            {
                possiblePositions.add(newPossiblePosition);
            }
        }

        return possiblePositions;
    }

    /*
        Given a direction and position piece, returns all possible positions for the piece using this direction
     */
    private Collection<BoardPosition> getPossiblePositionsForDirection(ChessDirectionVector directionVector, BoardPosition piecePosition)
    {
        T piece = getPieceByPosition(piecePosition);
        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();
        BoardPosition positionChange = getPositionChangeFromDirectionVector(directionVector, piece.getPlayer());

        BoardPosition newPosition = new BoardPosition(piecePosition);
        newPosition.addToPosition(positionChange);
        if(isPositionLegalToMoveOn(newPosition, piece.getPlayer()))
        {
            possiblePositions.add(newPosition);
            if(directionVector.isRepeating() && getPieceByPosition(newPosition) == null)
            {
                newPosition = new BoardPosition(newPosition);
                newPosition.addToPosition(positionChange);
                while(isPositionLegalToMoveOn(newPosition, piece.getPlayer()))
                {
                    possiblePositions.add(newPosition);
                    if(getPieceByPosition(newPosition) != null)
                    {
                        break; // In case a piece is captured
                    }
                    newPosition = new BoardPosition(newPosition);
                    newPosition.addToPosition(positionChange);
                }
            }
        }
        return possiblePositions;
    }

    /*
        Returns a position that represents a position change, from a chess direction vector
     */
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

    /*
        Checks if the given position on the board, owned by a given player,
        is under attack by a piece of a different player
     */
    private boolean isPositionUnderAttack(BoardPosition position, Player playerUnderAttack) throws InvalidPositionException
    {
        if(!(isPositionOnBoard(position)))
        {
            throw new IndexOutOfBoundsException("Position is out of bounds");
        }

        for(int x = 0; x < getBoard().length; x++)
        {
            for(int y = 0; y < getBoard()[0].length; y++)
            {
                T piece = getBoard()[x][y];
                if (piece != null && piece.getPlayer() != playerUnderAttack)
                {
                    BoardPosition piecePosition = new BoardPosition(x, y);
                    Collection<BoardPosition> possiblePositions = getPossiblePositionsForPiece(piecePosition);
                    if(possiblePositions.contains(position))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /*
        Checks if the piece at the given x position, needs to be promoted
     */
    private boolean pieceNeedsToBePromoted(int x, ChessPiece chessPiece)
    {
        return chessPiece instanceof Pawn && (x == 0 || x == getBoard().length - 1);
    }

    /*
        Checks if the given position is located on the board
     */
    private boolean isPositionOnBoard(BoardPosition position)
    {
        return isPositionOnBoard(position.getX(), position.getY());
    }

    /*
        Checks if the given position is located on the board
     */
    private boolean isPositionOnBoard(int x, int y)
    {
        return !(x < 0 || y < 0 || x >= getBoard().length || y >= getBoard()[0].length);
    }

    /*
        Checks if the given position is legal to move on, for a generic piece
     */
    private boolean isPositionLegalToMoveOn(BoardPosition position, Player movingPlayer)
    {
        return isPositionLegalToMoveOn(position.getX(), position.getY(), movingPlayer);
    }

    /*
        Checks if the given position is legal to move on, for a generic piece
     */
    private boolean isPositionLegalToMoveOn(int x, int y, Player movingPlayer)
    {
        return isPositionOnBoard(x,y) &&
                (getBoard()[x][y] == null || getBoard()[x][y].getPlayer() != movingPlayer);
    }


    private static final int WHITE_PAWN_START_X_POS = 6;
    private static final int BLACK_PAWN_START_X_POS = 1;
    /*
        Checks if the pawn at the given position has moved
     */
    private boolean hasPawnMoved(BoardPosition pawnPosition) throws InvalidPositionException
    {
        int x = pawnPosition.getX(), y = pawnPosition.getY();

        if(!isValidPawnPosition(pawnPosition))
        {
            throw new InvalidPositionException("Invalid board position for the pawn");
        }

        if(getPieceByPosition(pawnPosition).getPlayer() == Player.WHITE)
        {
            return x != WHITE_PAWN_START_X_POS;
        }
        else
        {
            return x != BLACK_PAWN_START_X_POS;
        }
    }

    /*
        Returns a piece, using it's positions on the board
     */
    private T getPieceByPosition(BoardPosition piecePosition)
    {
        return getBoard()[piecePosition.getX()][piecePosition.getY()];
    }

    /*
        Sets the board at the given position to the given piece (can be null)
     */
    private void setPieceByPosition(T piece, BoardPosition position)
    {
        getBoard()[position.getX()][position.getY()] = piece;
    }

    /*
        Checks if a given position is a valid piece position
     */
    private boolean isValidPiecePosition(BoardPosition position)
    {
        return isPositionOnBoard(position) && getPieceByPosition(position) != null;
    }

    /*
        Checks if the given position is a valid pawn position
     */
    private boolean isValidPawnPosition(BoardPosition position)
    {
        return isValidPiecePosition(position) && getPieceByPosition(position) instanceof Pawn;
    }

    /*
        Checks if the change in position was an en passant move
     */
    private boolean isEnPassantMove(BoardPosition oldPos, BoardPosition newPos)
    {
        return isValidPawnPosition(oldPos)
                && getPieceByPosition(newPos) == null
                && oldPos.getY() != newPos.getY();
    }


    private static final int PAWN_DOUBLE_MOVE_X_CHANGE = 2;
    /*
        Checks if the change in position was a double pawn move
     */
    private boolean isDoublePawnMove(BoardPosition oldPos, BoardPosition newPos)
    {
        return isValidPawnPosition(oldPos)
                && Math.abs(oldPos.getX() - newPos.getX()) == PAWN_DOUBLE_MOVE_X_CHANGE;
    }

    public BoardPosition getPositionOfDoubleMovedPawnLastTurn()
    {
        return positionOfDoubleMovedPawnLastTurn;
    }

    public void setPositionOfDoubleMovedPawnLastTurn(BoardPosition positionOfDoubleMovedPawnLastTurn)
    {
        this.positionOfDoubleMovedPawnLastTurn = positionOfDoubleMovedPawnLastTurn;
    }
}
