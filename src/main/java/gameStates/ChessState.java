package gameStates;

import boardgame.BoardPosition;
import directionVectors.ChessDirectionVector;
import enums.Player;
import exceptions.BoardGameException;
import exceptions.boardExceptions.InvalidPositionException;
import exceptions.boardExceptions.KingNotFoundException;
import pieces.chessPieces.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by divided on 19.03.2018.
 */
public class ChessState<T extends ChessPiece> extends BoardGameState<T>
{
    private static final int WHITE_PAWN_START_X_POS = 6;
    private static final int BLACK_PAWN_START_X_POS = 1;
    private static final int WHITE_ROOK_START_X_POS = 7;
    private static final int BLACK_ROOK_START_X_POS = 0;
    private static final int QUEEN_SIDE_ROOK_START_Y_POS = 0;
    private static final int KING_SIDE_ROOK_START_Y_POS = 7;
    private static final int PAWN_DOUBLE_MOVE_X_CHANGE = 2;


    private BoardPosition positionOfDoubleMovedPawnLastTurn;

    private ChessPieceMoveStatus moveStatus;

    private static BoardPosition whiteQueenSideRookStartPosition, whiteKingSideRookStartPosition,
            blackQueenSideRookStartPosition, blackKingSideRookStartPosition;

    public ChessState(T[][] board, Player playerToMove)
    {
        super(board, playerToMove);
        setMoveStatus(new ChessPieceMoveStatus());
        setRookStartPositions();
    }

    public ChessState(ChessState<T> state)
    {
        super(state);
        setMoveStatus(new ChessPieceMoveStatus(state.getMoveStatus()));
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
                        if(isMoveLegal(newState, piecePosition, possiblePosition))
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
        Returns if the move is legal (with the check and castling rules), for the move and the state after the move
     */
    public boolean isMoveLegal(ChessState<T> newState, BoardPosition currentPosition, BoardPosition newPosition) throws BoardGameException
    {
        if(isCastlingMove(currentPosition, newPosition))
        {
            if(!isValidCastle(currentPosition, newPosition))
            {
                return false;
            }
        }
        else
        {
            if(newState.kingIsUnderCheck(getPlayerToMove()))
            {
                return false;
            }
        }

        return true;
    }

    /*
        Returns all possible board positions for a single piece (including positions that cause checks)
     */
    public Collection<BoardPosition> getPossiblePositionsForPiece(BoardPosition piecePosition) throws InvalidPositionException
    {
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

        if(piece instanceof King)
        {
            possiblePositions.addAll(getCastlingPositions(piecePosition));
        }
        
        return possiblePositions;
    }

    /*
        Gets the new state after making a move on the current state
     */
    public ChessState<T> getStateAfterMove(BoardPosition currentPosition, BoardPosition newPosition) throws KingNotFoundException
    {
        ChessState<T> newState = new ChessState<T>(this);
        T pieceToMove = newState.getPieceByPosition(currentPosition);
        newState.setPlayerToMove(Player.getOppositePlayer(pieceToMove.getPlayer()));
        changeNewStateBasedOnSpecialPawnMoves(newState, currentPosition, newPosition);
        newState.setMoveStatus(getUpdatedMoveStatus(getMoveStatus(), currentPosition));

        if(isCastlingMove(currentPosition, newPosition))
        {
            return getNewStateAfterCastling(newState, currentPosition, newPosition);
        }

        newState.setPieceByPosition(pieceToMove, newPosition);
        newState.setPieceByPosition(null , currentPosition);

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
        return isPositionUnderAttack(getKingPosition(kingsPlayer), kingsPlayer);
    }

    /*
       Gets the state result
       State result includes an indication if the game is finished, and the winning player (null if one doesn't exist)
    */
    public StateResult getStateResult() throws BoardGameException
    {
        Collection<BoardGameState<T>> possibleStates = getAllPossibleStates();
        if (possibleStates.isEmpty())
        {
            if(kingIsUnderCheck(getPlayerToMove()))
            {
                Player winner = Player.getOppositePlayer(getPlayerToMove());
                return new StateResult(true, winner);
            }
            else
            {
                return new StateResult(true, null);
            }
        }
        else
        {
            return new StateResult(false, null);
        }
    }

    /*
        Returns all positions for all pieces, for the given player
     */
    public Collection<BoardPosition> getAllPositionsForPlayer(Player player) throws InvalidPositionException
    {
        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();
        for(int x = 0; x < getBoard().length; x++)
        {
            for (int y = 0; y < getBoard()[0].length; y++)
            {
                T piece = getBoard()[x][y];
                if (piece != null && piece.getPlayer() == player)
                {
                    possiblePositions.addAll(getPossiblePositionsForPiece(new BoardPosition(x, y)));
                }
            }
        }

        return possiblePositions;
    }

    /*
        Checks if a given position is a valid piece position
     */
    public boolean isValidPiecePosition(BoardPosition position)
    {
        return isPositionOnBoard(position) && getPieceByPosition(position) != null;
    }

    /*
        Sets the starting positions for the rook
     */
    protected static void setRookStartPositions()
    {
        whiteQueenSideRookStartPosition = new BoardPosition(WHITE_ROOK_START_X_POS, QUEEN_SIDE_ROOK_START_Y_POS);
        whiteKingSideRookStartPosition = new BoardPosition(WHITE_ROOK_START_X_POS, KING_SIDE_ROOK_START_Y_POS);
        blackQueenSideRookStartPosition = new BoardPosition(BLACK_ROOK_START_X_POS, QUEEN_SIDE_ROOK_START_Y_POS);
        blackKingSideRookStartPosition = new BoardPosition(BLACK_ROOK_START_X_POS, KING_SIDE_ROOK_START_Y_POS);
    }


    /*
        Gets the new state after castling
     */
    private ChessState<T> getNewStateAfterCastling(ChessState<T> newState, BoardPosition currentKingPosition, BoardPosition newKingPosition)
    {
        BoardPosition currentRookPosition = getRookPositionBeforeCastle(currentKingPosition, newKingPosition);
        BoardPosition newRookPosition = getRookPositionAfterCastle(currentKingPosition, newKingPosition);
        T king = getPieceByPosition(currentKingPosition);
        T rook = getPieceByPosition(currentRookPosition);
        newState.setPieceByPosition(null, currentKingPosition);
        newState.setPieceByPosition(null, currentRookPosition);
        newState.setPieceByPosition(king, newKingPosition);
        newState.setPieceByPosition(rook, newRookPosition);
        return newState;
    }

    /*
       Gets the rook position for a king's castling move before castling
    */
    private BoardPosition getRookPositionBeforeCastle(BoardPosition currentKingPosition, BoardPosition newKingPosition)
    {
        T king = getPieceByPosition(currentKingPosition);
        boolean isKingSideCastling = isMoveCastlingKingSide(currentKingPosition, newKingPosition);
        BoardPosition rookPosition;

        if(king.getPlayer() == Player.WHITE)
        {
            if(isKingSideCastling)
            {
                rookPosition = getWhiteKingSideRookStartPosition();
            }
            else
            {
                rookPosition = getWhiteQueenSideRookStartPosition();
            }
        }
        else
        {
            if(isKingSideCastling)
            {
                rookPosition = getBlackKingSideRookStartPosition();
            }
            else
            {
                rookPosition = getBlackQueenSideRookStartPosition();
            }
        }

        return rookPosition;
    }

    /*
       Gets the rook position for a king's castling move after castling
    */
    private BoardPosition getRookPositionAfterCastle(BoardPosition currentKingPosition, BoardPosition newKingPosition)
    {
        T king = getPieceByPosition(currentKingPosition);
        boolean isKingSideCastling = isMoveCastlingKingSide(currentKingPosition, newKingPosition);
        BoardPosition rookPosition;

        if(king.getPlayer() == Player.WHITE)
        {
            if(isKingSideCastling)
            {
                rookPosition = Rook.getWhiteKingSideCastlePosition();
            }
            else
            {
                rookPosition = Rook.getWhiteQueenSideCastlePosition();
            }
        }
        else
        {
            if(isKingSideCastling)
            {
                rookPosition = Rook.getBlackKingSideCastlePosition();
            }
            else
            {
                rookPosition = Rook.getBlackQueenSideCastlePosition();
            }
        }

        return rookPosition;
    }

    private boolean isMoveCastlingKingSide(BoardPosition currentKingPosition, BoardPosition newKingPosition)
    {
        return newKingPosition.getY() > currentKingPosition.getY();
    }

    /*
       Changes the new state based on special pawn moves (en-passant and double move)
    */
    private ChessState<T> changeNewStateBasedOnSpecialPawnMoves(ChessState<T> newState, BoardPosition currentPosition,
                                                                BoardPosition newPosition)
    {
        if(isEnPassantMove(currentPosition, newPosition))
        {
            newState.setPieceByPosition(null, getPositionOfDoubleMovedPawnLastTurn());
        }
        else if(isDoublePawnMove(currentPosition, newPosition))
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
        Gets all castling positions for the king
     */
    private Collection<BoardPosition> getCastlingPositions(BoardPosition kingPosition) throws InvalidPositionException
    {
        if(!isValidPiecePosition(kingPosition) || !(getPieceByPosition(kingPosition) instanceof King))
        {
            throw new InvalidPositionException("Invalid position for the king");
        }

        Collection<BoardPosition> possiblePositions = new ArrayList<BoardPosition>();
        King king = (King)getPieceByPosition(kingPosition);
        if(king.getPlayer() == Player.WHITE && !getMoveStatus().hasWhiteKingMoved())
        {
            if(!getMoveStatus().hasWhiteKingSideRookMoved())
            {
                possiblePositions.add(new BoardPosition(King.getWhiteKingSideCastlePosition()));
            }

            if(!getMoveStatus().hasWhiteQueenSideRookMoved())
            {
                possiblePositions.add(new BoardPosition(King.getWhiteQueenSideCastlePosition()));
            }
        }
        else if (king.getPlayer() == Player.BLACK && !getMoveStatus().hasBlackKingMoved())
        {
            if(!getMoveStatus().hasBlackKingSideRookMoved())
            {
                possiblePositions.add(new BoardPosition(King.getBlackKingSideCastlePosition()));
            }

            if(!getMoveStatus().hasBlackQueenSideRookMoved())
            {
                possiblePositions.add(new BoardPosition(King.getBlackQueenSideCastlePosition()));
            }
        }

        return possiblePositions;
    }

    /*
        Checks if the king can castle with his move
     */
    private boolean isValidCastle(BoardPosition currentKingPosition, BoardPosition newKingPosition) throws InvalidPositionException
    {
        Player player = getPieceByPosition(currentKingPosition).getPlayer();
        Collection<BoardPosition> positionsToCheck = getPositionsBetweenTwoOnSameRow(currentKingPosition, newKingPosition);
        if(areAllPositionsEmpty(positionsToCheck))
        {
            positionsToCheck.add(currentKingPosition);
            positionsToCheck.add(newKingPosition);
            return !areAnyPositionsUnderAttack(positionsToCheck, player);
        }

        return false;
    }

    /*
        Checks if a move is a castling move
     */
    private boolean isCastlingMove(BoardPosition currentPosition, BoardPosition newPosition)
    {
        T pieceToMove = getPieceByPosition(currentPosition);
        T pieceAtNewPosition = getPieceByPosition(newPosition);
        if(pieceToMove instanceof King)
        {
            if(Math.abs(newPosition.getY() - currentPosition.getY()) > 1 ||
                (pieceAtNewPosition instanceof Rook && pieceAtNewPosition.getPlayer() == pieceToMove.getPlayer()))
            {
                return true;
            }
        }

        return false;
    }

    /*
        Checks if all the given positions are empty
     */
    private boolean areAllPositionsEmpty(Collection<BoardPosition> positions)
    {
        for(BoardPosition position : positions)
        {
            if(getPieceByPosition(position) != null)
            {
                return false;
            }
        }
        return true;
    }

    /*
        Given two positions, returns all positions between them
     */
    private Collection<BoardPosition> getPositionsBetweenTwoOnSameRow(BoardPosition firstPosition, BoardPosition secondPosition) throws InvalidPositionException
    {
        if(firstPosition.getX() != secondPosition.getX())
        {
            throw new InvalidPositionException("The positions checked must be on the same row");
        }
        BoardPosition startPosition, endPosition;
        if(firstPosition.getY() <= secondPosition.getY())
        {
            startPosition = new BoardPosition(firstPosition);
            endPosition = new BoardPosition(secondPosition);
        }
        else
        {
            startPosition = new BoardPosition(secondPosition);
            endPosition = new BoardPosition(firstPosition);
        }

        Collection<BoardPosition> positions = new ArrayList<BoardPosition>();
        startPosition.addToPosition(0, 1);
        while(!(startPosition.equals(endPosition)))
        {
            if(!isPositionOnBoard(startPosition))
            {
                throw new InvalidPositionException("At least one of the positions between two is outside of the board");
            }
            positions.add(new BoardPosition(startPosition));
            startPosition.addToPosition(0, 1);
        }
        return positions;
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
        Returns the position of the king, for the given player
     */
    private BoardPosition getKingPosition(Player kingsPlayer) throws KingNotFoundException
    {
        for(int x = 0; x < getBoard().length; x++)
        {
            for (int y = 0; y < getBoard()[0].length; y++)
            {
                T piece = getBoard()[x][y];
                if (piece != null && piece.getPlayer() == kingsPlayer && piece instanceof King)
                {
                    return new BoardPosition(x, y);
                }
            }
        }

        throw new KingNotFoundException("King not found on board");
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
        Checks if the given position is under attack by the opponent
     */
    private boolean isPositionUnderAttack(BoardPosition position, Player playerUnderAttack) throws InvalidPositionException
    {
        Player opponent = Player.getOppositePlayer(playerUnderAttack);
        Collection<BoardPosition> opponentPossiblePositions = getAllPositionsForPlayer(opponent);
        return opponentPossiblePositions.contains(position);
    }

    /*
        Checks if any of the given positions is under attack by the opponent
     */
    private boolean areAnyPositionsUnderAttack(Collection<BoardPosition> positions, Player playerUnderAttack) throws InvalidPositionException
    {
        Player opponent = Player.getOppositePlayer(playerUnderAttack);
        Collection<BoardPosition> opponentPossiblePositions = getAllPositionsForPlayer(opponent);
        for(BoardPosition position : positions)
        {
            if(opponentPossiblePositions.contains(position))
            {
                return true;
            }
        }

        return false;
    }

    /*
        Gets the updated move status, based on the move
     */
    private ChessPieceMoveStatus getUpdatedMoveStatus(ChessPieceMoveStatus currentStatus, BoardPosition movePosition) throws KingNotFoundException
    {
        ChessPieceMoveStatus newMoveStatus = new ChessPieceMoveStatus(currentStatus);
        T piece = getPieceByPosition(movePosition);
        if(piece instanceof King)
        {
            if(piece.getPlayer() == Player.WHITE)
            {
                newMoveStatus.setHasWhiteKingMoved(true);
            }
            else
            {
                newMoveStatus.setHasBlackKingMoved(true);
            }
        }
        else if(piece instanceof Rook)
        {
            BoardPosition kingPosition = getKingPosition(piece.getPlayer());
            boolean isKingSideRook;
            if(kingPosition.getY() < movePosition.getY())
            {
                isKingSideRook = true;
            }
            else
            {
                isKingSideRook = false;
            }

            if(piece.getPlayer() == Player.WHITE)
            {
                if(isKingSideRook)
                {
                    newMoveStatus.setHasWhiteKingSideRookMoved(true);
                }
                else
                {
                    newMoveStatus.setHasWhiteQueenSideRookMoved(true);
                }
            }
            else
            {
                if(isKingSideRook)
                {
                    newMoveStatus.setHasBlackKingSideRookMoved(true);
                }
                else
                {
                    newMoveStatus.setHasBlackQueenSideRookMoved(true);
                }
            }
        }

        return newMoveStatus;
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
        Sets the board at the given position to the given piece (can be null)
     */
    private void setPieceByPosition(T piece, BoardPosition position)
    {
        getBoard()[position.getX()][position.getY()] = piece;
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
    private boolean isEnPassantMove(BoardPosition currentPosition, BoardPosition newPosition)
    {
        return isValidPawnPosition(currentPosition)
                && getPieceByPosition(newPosition) == null
                && currentPosition.getY() != newPosition.getY();
    }

    /*
        Checks if the change in position was a double pawn move
     */
    private boolean isDoublePawnMove(BoardPosition currentPosition, BoardPosition newPosition)
    {
        return isValidPawnPosition(currentPosition)
                && Math.abs(currentPosition.getX() - newPosition.getX()) == PAWN_DOUBLE_MOVE_X_CHANGE;
    }

    /*
        Returns a piece, using it's positions on the board
     */
    private T getPieceByPosition(BoardPosition piecePosition)
    {
        return getBoard()[piecePosition.getX()][piecePosition.getY()];
    }

    private BoardPosition getPositionOfDoubleMovedPawnLastTurn()
    {
        return positionOfDoubleMovedPawnLastTurn;
    }

    private void setPositionOfDoubleMovedPawnLastTurn(BoardPosition positionOfDoubleMovedPawnLastTurn)
    {
        this.positionOfDoubleMovedPawnLastTurn = positionOfDoubleMovedPawnLastTurn;
    }

    private ChessPieceMoveStatus getMoveStatus()
    {
        return moveStatus;
    }

    private void setMoveStatus(ChessPieceMoveStatus moveStatus)
    {
        this.moveStatus = moveStatus;
    }

    private static BoardPosition getWhiteQueenSideRookStartPosition()
    {
        return whiteQueenSideRookStartPosition;
    }

    private static BoardPosition getWhiteKingSideRookStartPosition()
    {
        return whiteKingSideRookStartPosition;
    }

    private static BoardPosition getBlackQueenSideRookStartPosition()
    {
        return blackQueenSideRookStartPosition;
    }

    private static BoardPosition getBlackKingSideRookStartPosition()
    {
        return blackKingSideRookStartPosition;
    }
}
