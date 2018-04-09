package directionVectors;

import java.util.ArrayList;

/**
 * Created by divided on 19.03.2018.
 */
public class ChessDirectionVector extends DirectionVector
{
    private boolean isRepeating;

    public ChessDirectionVector(boolean isRepeating, int up, int right)
    {
        this.isRepeating = isRepeating;
        setDirections(up, right);
    }

    public boolean isRepeating()
    {
        return isRepeating;
    }


    public void setDirections(int up, int right)
    {
        ArrayList<Integer> directions = new ArrayList<Integer>();
        directions.add(up);
        directions.add(right);
        super.setDirections(directions);
    }
}
