package ghost;

/**
 * Eatable is an interface that contains one behaviour method.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public interface Eatable {
    /**
     * Eats this Eatable object after colliding with Image sprite.
     * @param sprite    Image sprite.
     */
    public void eaten(Image sprite);
}
