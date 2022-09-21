package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;
import unit.Square;

/**
 * class the represents the Knight Piece in chess
 * 
 * @author Jackson Shortell
 */
public class Knight extends Piece {

    private static final int VALUE = 3;
    private static final String NAME = "Knight";

    public Knight(Coordinate position, Team color) {
        super(VALUE, position, color, NAME);
    }

    /**
     * copy constructor
     * 
     * @param template passes in a Knight to make a copy of
     */
    public Knight(Knight template) {
        super(template.getValue(), template.getPosition(), template.getTeam(), template.getName());
    }

    /**
     * Overrides the symbol function to return N instead of K
     */
    @Override
    public String symbol() {
        return color.getAnsi() + "N" + "\u001B[37m";
    }

    @Override
    public TreeSet<Coordinate> allValidMoves(Board board) {
        TreeSet<Coordinate> validMoves = new TreeSet<>();
        TreeSet<Coordinate> possibleMoves = allPossibleMoves();
        for (Coordinate coord : possibleMoves) {
            Square square = board.getSquare(coord);
            if (isSquareValid(square)) {
                validMoves.add(coord);
            }
        }
        return validMoves;
    }

    @Override
    public TreeSet<Coordinate> allValidDefendedSquares(Board board) {
        return allPossibleMoves();
    }

    /**
     * HELPER FUNCTION
     * determines if a Square is valid to move to, a Square is valid to move to if
     * it is empty or the Piece occupying is the opposite color
     * 
     * @param square passes in the square to check
     * @return returns true if the square is valid to move to and false otherwise
     */
    private boolean isSquareValid(Square square) {
        if (square.isEmpty()) {
            return true;
        } else {
            Piece piece = square.getPiece();
            return color != piece.getTeam();
        }
    }

    /**
     * HELPER FUNCTION
     * produces a TreeSet of all possible moves for the Knight
     * 
     * @return returns a Tree Set of Coordinates
     */
    public TreeSet<Coordinate> allPossibleMoves() {
        TreeSet<Coordinate> moves = new TreeSet<>();
        int one = 1;
        int two = 2;
        addCoordToTreeSet(moves, one, two);
        addCoordToTreeSet(moves, two, one);
        addCoordToTreeSet(moves, one, -two);
        addCoordToTreeSet(moves, two, -one);
        addCoordToTreeSet(moves, -one, two);
        addCoordToTreeSet(moves, -two, one);
        addCoordToTreeSet(moves, -one, -two);
        addCoordToTreeSet(moves, -two, -one);
        return moves;
    }

    /**
     * HELPER FUNCTION
     * given a set of Coordinates and a int for file and rank change produces a
     * Coordinate, if the Coordinate is not null adds it to the set
     * 
     * @param set
     * @param fileChange
     * @param rankChange
     */
    private void addCoordToTreeSet(TreeSet<Coordinate> set, int fileChange, int rankChange) {
        Coordinate coordinate = singleMove(fileChange, rankChange);
        if (coordinate != null) {
            set.add(coordinate);
        }
    }

    /**
     * HELPER FUNCTION
     * produces a Coordinate that could be a legal move for the knight
     * 
     * @param fileChange the change in the file as an int
     * @param rankChange the change in the rank as an int
     * @return returns a Coordinate, or null if trying to move off the board
     */
    private Coordinate singleMove(int fileChange, int rankChange) {
        int fileIndex = position.getFileIndex();
        int rankIndex = position.getRankIndex();
        return Coordinate.getCoordinate(fileIndex + fileChange, rankIndex + rankChange);
    }

}