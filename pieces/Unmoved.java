package pieces;

/**
 * interface for Pieces that have different behavior whether they have made
 * their first move or not such as the pawn, rook, and king
 * 
 *  @author Jackson Shortell
 */
public interface Unmoved {

    /**
     * @return true if the piece has not moved, false otherwise
     */
    public boolean getUnmoved();

    /**
     * sets the Unmoved boolean to false
     */
    public void setUnmovedFalse();

}