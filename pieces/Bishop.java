package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;

/**
 * class the represents the Bishop Piece in chess
 * 
 *  @author Jackson Shortell
 */
public class Bishop extends Piece {

    private static final int VALUE = 3;
    private static final String NAME = "Bishop";

    public Bishop(Coordinate position, Team color) {
        super(VALUE, position, color, NAME);
    }

    /**
     * copy constructor
     * 
     * @param template passes in a Bishop to make a copy of
     */
    public Bishop(Bishop template) {
        super(template.getValue(), template.getPosition(), template.getTeam(), template.getName());
    }

    @Override
    public TreeSet<Coordinate> allValidMoves(Board board) {
        return Direction.validDiagonalMoves(board, position, color);
    }

    @Override
    public TreeSet<Coordinate> allValidDefendedSquares(Board board) {
        return Direction.validDiagonalDefends(board, position);
    }

}