package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;

/**
 * class that represents the Rook Piece in chess
 * 
 * @author Jackson Shortell
 */
public class Rook extends Piece implements Unmoved {

    /**
     * boolean that represents if the Rook has made its first move
     */
    private boolean Unmoved;

    private static final int VALUE = 5;
    private static final String NAME = "Rook";

    public Rook(Coordinate position, Team color) {
        super(VALUE, position, color, NAME);
        this.Unmoved = true;
    }

    /**
     * copy constructor
     * 
     * @param template passes in a Rook to make a copy of
     */
    public Rook(Rook template) {
        super(template.getValue(), template.getPosition(), template.getTeam(), template.getName());
    }

    @Override
    public TreeSet<Coordinate> allValidMoves(Board board) {
        return Direction.validLinearMoves(board, position, color);
    }

    @Override
    public TreeSet<Coordinate> allValidDefendedSquares(Board board) {
        return Direction.validLinearDefends(board, position);
    }

    public boolean getUnmoved() {
        return this.Unmoved;
    }

    public void setUnmovedFalse() {
        this.Unmoved = false;
    }

}