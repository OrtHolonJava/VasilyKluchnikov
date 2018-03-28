package boardgame;

/**
 * Created by divided on 24.03.2018.
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

    public void addToPosition(int x, int y)
    {
        this.x += x;
        this.y += y;
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

    public void multiplyPosition(int n)
    {
        x *= n;
        y *= n;
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
