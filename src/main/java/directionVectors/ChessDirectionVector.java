package directionVectors;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A chess direction vector
 * Each vector consists of up and right movement (can be negative), and whether the movement can repeat
 */
public class ChessDirectionVector extends DirectionVector
{
    private boolean isRepeating;

    public ChessDirectionVector(boolean isRepeating, int up, int right)
    {
        this.isRepeating = isRepeating;
        setDirections(up, right);
    }

    /*
        Indicates if a direction can repeat in the same direction if certain chess rules are met
     */
    public boolean isRepeating()
    {
        return isRepeating;
    }

    /*
        Chess direction vector only uses 2 integers:
        First is the change in the up direction
        Second is the change in the right direction
     */
    public void setDirections(int up, int right)
    {
        Collection<Integer> directions = new ArrayList<Integer>();
        directions.add(up);
        directions.add(right);
        super.setDirections(directions);
    }
}
