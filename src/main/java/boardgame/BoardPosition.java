package boardgame;

/**
 * Position on a board (x,y)
 */
public class BoardPosition
{
    private int x, y;

    public BoardPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public BoardPosition(BoardPosition position)
    {
        this.x = position.x;
        this.y = position.y;
    }

    public void addToPosition(BoardPosition position)
    {
        addToPosition(position.getX(), position.getY());
    }

    /*
        Adds to the x and y, respectively
     */
    public void addToPosition(int x, int y)
    {
        this.x += x;
        this.y += y;
    }

    /*
        Multiplies both x and y by n
     */
    public void multiplyPosition(int n)
    {
        x *= n;
        y *= n;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof BoardPosition))
        {
            return false;
        }

        BoardPosition position = (BoardPosition)obj;
        return x == position.x && y == position.y;
    }

    public int getX()
    {
        return x;
    }

    public BoardPosition setX(int x)
    {
        this.x = x;
        return this;
    }

    public int getY()
    {
        return y;
    }

    public BoardPosition setY(int y)
    {
        this.y = y;
        return this;
    }
}
