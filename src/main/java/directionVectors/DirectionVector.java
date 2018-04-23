package directionVectors;

import java.util.Collection;

/**
 * A direction vector, which holds a collection of integers which represent a direction
 */
public class DirectionVector
{
    private Collection<Integer> directions;

    public Collection<Integer> getDirections()
    {
        return directions;
    }

    public void setDirections(Collection<Integer> directions)
    {
        this.directions = directions;
    }
}
