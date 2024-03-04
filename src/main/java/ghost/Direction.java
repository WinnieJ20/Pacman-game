package ghost;

/**
 * Directions is an enum class that can be used
 * to specify direction.
 * {@link #LEFT}
 * {@link #RIGHT}
 * {@link #UP}
 * {@link #DOWN}
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public enum Direction {
    /**
     * LEFT direction
     */
    LEFT,

    /**
     * UP direction
     */
    UP,

    /**
     * RIGHT direction
     */
    RIGHT,

    /**
     * DOWN direction
     */
    DOWN;

    /**
     * Gets the opposition direction of this Direction.
     * @return      Direction that is in the opposite way of
     *              this Direction
     */
    public Direction opposite(){
        if (this == Direction.LEFT){
            return Direction.RIGHT;
        }else if (this == Direction.RIGHT){
            return Direction.LEFT;
        }else if (this == Direction.UP){
            return Direction.DOWN;
        }else{
            return Direction.UP;
        }
    }
}
