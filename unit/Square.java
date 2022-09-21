package unit;

import java.util.TreeSet;

import pieces.Piece;

/**
 * class that represents a single Square on a chess board
 * 
 * @author Jackson Shortell
 */
public class Square {

    /**
     * Piece that is on the Square, null if Square is empty
     */
    private Piece piece;
    /**
     * the Color of the pieces that defend the Square
     */
    private TreeSet<Team> defendedBy;

    public Square() {
        this.piece = null;
        this.defendedBy = new TreeSet<>();
    }

    /**
     * copy constructor
     * 
     * @param template passes in a Square to make a deep copy of
     */
    public Square(Square template) {
        this.piece = Piece.copyPiece(template.getPiece());
        this.defendedBy = new TreeSet<>();
        TreeSet<Team> original = template.getDefendedBy();
        for (Team color : original) {
            this.defendedBy.add(color);
        }
    }

    public Piece getPiece() {
        return piece == null ? null : piece;
    }

    public TreeSet<Team> getDefendedBy() {
        return defendedBy;
    }

    /**
     * gets the Color of the Piece occupying the Square
     * 
     * @return returns a Color, null if there is no Piece
     */
    public Team getPieceColor() {
        return piece != null ? piece.getTeam() : null;
    }

    /**
     * @return true if the Piece on the Square is NOT null, false otherwise
     */
    public boolean isOccupied() {
        return piece != null;
    }

    /**
     * @return true if the Piece on the Square IS null, false otherwise
     */
    public boolean isEmpty() {
        return piece == null;
    }

    /**
     * places the passed in piece onto the Square
     * sets the Square's piece field as the passed in piece
     * returns the Piece that was previously on the Square
     * 
     * @param piece passes in a Piece to place
     * @return Piece that was captured
     */
    public Piece setPiece(Piece piece) {
        Piece temp = this.piece;
        this.piece = piece;
        return temp;
    }

    /**
     * removes the Piece from the Square and sets the Squares Piece field to null
     * 
     * @return Piece that was previously on the Square
     */
    public Piece removePiece() {
        return setPiece(null);
    }

    /**
     * adds a Color to the Squares defendedBy TreeSet
     * 
     * @param color passes in a color to add
     */
    public void addDefendedBy(Team color) {
        this.defendedBy.add(color);
    }

    /**
     * removes all Colors from the Squares defendedBy set
     */
    public void clearDefendBy() {
        this.defendedBy.remove(Team.WHITE);
        this.defendedBy.remove(Team.BLACK);
    }

    @Override
    public String toString() {
        String str = isOccupied() ? piece.symbol() : " ";
        return "[" + str + "]";
    }

}