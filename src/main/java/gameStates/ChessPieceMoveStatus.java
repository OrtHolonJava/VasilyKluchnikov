package gameStates;

/**
 * A class which contains move statuses of specific chess pieces (whether they have moved)
 */
public class ChessPieceMoveStatus
{
    private boolean hasWhiteKingMoved, hasBlackKingMoved, hasWhiteKingSideRookMoved, hasBlackKingSideRookMoved,
            hasWhiteQueenSideRookMoved, hasBlackQueenSideRookMoved;

    public ChessPieceMoveStatus(boolean hasWhiteKingMoved, boolean hasBlackKingMoved, boolean hasWhiteKingSideRookMoved,
                                boolean hasBlackKingSideRookMoved, boolean hasWhiteQueenSideRookMoved, boolean hasBlackQueenSideRookMoved)
    {
        this.hasWhiteKingMoved = hasWhiteKingMoved;
        this.hasBlackKingMoved = hasBlackKingMoved;
        this.hasWhiteKingSideRookMoved = hasWhiteKingSideRookMoved;
        this.hasBlackKingSideRookMoved = hasBlackKingSideRookMoved;
        this.hasWhiteQueenSideRookMoved = hasWhiteQueenSideRookMoved;
        this.hasBlackQueenSideRookMoved = hasBlackQueenSideRookMoved;
    }

    public ChessPieceMoveStatus(ChessPieceMoveStatus moveStatus)
    {
        this(moveStatus.hasWhiteKingMoved, moveStatus.hasBlackKingMoved, moveStatus.hasWhiteKingSideRookMoved,
                moveStatus.hasBlackKingSideRookMoved, moveStatus.hasWhiteQueenSideRookMoved, moveStatus.hasBlackQueenSideRookMoved);
    }

    public ChessPieceMoveStatus()
    {
        this(false, false, false, false, false, false);
    }

    public boolean hasWhiteKingMoved()
    {
        return hasWhiteKingMoved;
    }

    public void setHasWhiteKingMoved(boolean hasWhiteKingMoved)
    {
        this.hasWhiteKingMoved = hasWhiteKingMoved;
    }

    public boolean hasBlackKingMoved()
    {
        return hasBlackKingMoved;
    }

    public void setHasBlackKingMoved(boolean hasBlackKingMoved)
    {
        this.hasBlackKingMoved = hasBlackKingMoved;
    }

    public boolean hasWhiteKingSideRookMoved()
    {
        return hasWhiteKingSideRookMoved;
    }

    public void setHasWhiteKingSideRookMoved(boolean hasWhiteKingSideRookMoved)
    {
        this.hasWhiteKingSideRookMoved = hasWhiteKingSideRookMoved;
    }

    public boolean hasBlackKingSideRookMoved()
    {
        return hasBlackKingSideRookMoved;
    }

    public void setHasBlackKingSideRookMoved(boolean hasBlackKingSideRookMoved)
    {
        this.hasBlackKingSideRookMoved = hasBlackKingSideRookMoved;
    }

    public boolean hasWhiteQueenSideRookMoved()
    {
        return hasWhiteQueenSideRookMoved;
    }

    public void setHasWhiteQueenSideRookMoved(boolean hasWhiteQueenSideRookMoved)
    {
        this.hasWhiteQueenSideRookMoved = hasWhiteQueenSideRookMoved;
    }

    public boolean hasBlackQueenSideRookMoved()
    {
        return hasBlackQueenSideRookMoved;
    }

    public void setHasBlackQueenSideRookMoved(boolean hasBlackQueenSideRookMoved)
    {
        this.hasBlackQueenSideRookMoved = hasBlackQueenSideRookMoved;
    }
}
