package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;

/**
 * class that represents the Queen Piece in chess
 * 
 * @author Jackson Shortell
 */
public class Queen extends Piece {

    private static final int VALUE = 9;
    private static final String NAME = "Queen";

    public Queen(Coordinate position, Team color) {
        super(VALUE, position, color, NAME);
    }

    /**
     * copy constructor
     * 
     * @param template passes in a Queen to make a copy of
     */
    public Queen(Queen template) {
        super(template.getValue(), template.getPosition(), template.getTeam(), template.getName());
    }

    @Override
    public TreeSet<Coordinate> allValidMoves(Board board) {
        TreeSet<Coordinate> moves = Direction.validLinearMoves(board, position, color);
        moves.addAll(Direction.validDiagonalMoves(board, position, color));
        return moves;
    }

    @Override
    public TreeSet<Coordinate> allValidDefendedSquares(Board board) {
        TreeSet<Coordinate> defends = Direction.validLinearDefends(board, position);
        defends.addAll(Direction.validDiagonalDefends(board, position));
        return defends;
    }

}