package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;
import unit.File;
import unit.Move;
import unit.Rank;
import unit.Square;

/**
 * class that represents the Pawn piece in chess
 * 
 * @author Jackson Shortell
 */
public class Pawn extends Piece implements Unmoved {

    /**
     * boolean that represents if the pawn has made its first move
     */
    private boolean Unmoved;

    private static final int VALUE = 1;
    private static final String NAME = "Pawn";

    public Pawn(Coordinate position, Team color) {
        super(VALUE, position, color, NAME);
        this.Unmoved = true;
    }

    /**
     * copy constructor
     * 
     * @param template passes in a Pawn to make a copy of
     */
    public Pawn(Pawn template) {
        super(template.getValue(), template.getPosition(), template.getTeam(), template.getName());
        this.Unmoved = template.getUnmoved();
    }

    @Override
    public TreeSet<Coordinate> allValidMoves(Board board) {
        TreeSet<Coordinate> moves = allMoves(board);
        Coordinate leftDefend = leftCapture();
        if (leftDefend != null) {
            Square upLeft = board.getSquare(leftDefend);
            if ((upLeft.isOccupied() && upLeft.getPieceColor() != color) || isLeftEnPassantValid(board)) {
                moves.add(leftDefend);
            }
        }
        Coordinate rightDefend = rightCapture();
        if (rightDefend != null) {
            Square upRight = board.getSquare(rightDefend);
            if ((upRight.isOccupied() && upRight.getPiece().getTeam() != color) || isRightEnPassantValid(board)) {
                moves.add(rightDefend);
            }
        }
        return moves;
    }

    @Override
    public TreeSet<Coordinate> allValidDefendedSquares(Board board) {
        TreeSet<Coordinate> defends = new TreeSet<>();

        Coordinate cord = leftCapture();

        if (cord != null) {
            defends.add(cord);
        }

        cord = rightCapture();
        if (cord != null) {
            defends.add(cord);
        }

        return defends;
    }

    /**
     * HELPER FUNCTION
     * gets the one or two non capturing moves the pawn can make
     * 
     * @param board passes in the current state of the board
     * @return returns a TreeSet of Coordinates
     */
    private TreeSet<Coordinate> allMoves(Board board) {
        TreeSet<Coordinate> moves = new TreeSet<>();
        int rankIndex = position.getRankIndex();
        int fileIndex = position.getFileIndex();

        int one = (color == Team.WHITE) ? 1 : -1;
        int two = (color == Team.WHITE) ? 2 : -2;
        if (rankIndex != 7 && rankIndex != 0) {
            Coordinate move = Coordinate.getCoordinate(fileIndex, rankIndex + one);
            Square oneInFront = board.getSquare(move);
            if (oneInFront.isEmpty()) {
                moves.add(move);
            }
            if (Unmoved) {
                move = Coordinate.getCoordinate(fileIndex, rankIndex + two);
                Square twoInFront = board.getSquare(move);
                if (oneInFront.isEmpty() && twoInFront.isEmpty()) {
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    /**
     * HELPER FUNCTION
     * determines if an en passant move is valid to the left
     * 
     * @param board passes in the current state of the board
     * @return returns true if en passant left is valid and false other wise
     */
    private boolean isLeftEnPassantValid(Board board) {
        Move lastMove = board.getLastMove();
        if (lastMove.isFromAndToNotNull()) {
            Coordinate to = lastMove.getTo();
            Piece piece = board.getSquare(to).getPiece();

            File leftOf = File.indexToFile(position.getFile().getIndex() - 1);

            Rank expectedFromRank = color == Team.WHITE ? Rank.SEVEN : Rank.TWO;
            Rank expectedToRank = color == Team.WHITE ? Rank.FIVE : Rank.FOUR;

            if (position.getRank() == expectedToRank) {
                if (leftOf != null) {
                    Coordinate expectedFrom = Coordinate.getCoordinate(leftOf, expectedFromRank);
                    Coordinate expectedTo = Coordinate.getCoordinate(leftOf, expectedToRank);
                    if (expectedFrom == lastMove.getFrom() && expectedTo == to
                            && piece instanceof Pawn) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * HELPER FUNCTION
     * determines if an en passant move is valid to the right
     * 
     * @param board passes in the current state of the board
     * @return returns true if en passant right is valid and false other wise
     */
    private boolean isRightEnPassantValid(Board board) {
        Move lastMove = board.getLastMove();
        if (lastMove.isFromAndToNotNull()) {
            Coordinate to = lastMove.getTo();
            Piece piece = board.getSquare(to).getPiece();

            File rightOf = File.indexToFile(position.getFile().getIndex() + 1);

            Rank expectedFromRank = color == Team.WHITE ? Rank.SEVEN : Rank.TWO;
            Rank expectedToRank = color == Team.WHITE ? Rank.FIVE : Rank.FOUR;
            if (position.getRank() == expectedToRank) {
                if (rightOf != null) {
                    Coordinate expectedFrom = Coordinate.getCoordinate(rightOf, expectedFromRank);
                    Coordinate expectedTo = Coordinate.getCoordinate(rightOf, expectedToRank);
                    if (expectedFrom == lastMove.getFrom() && expectedTo == to
                            && piece instanceof Pawn) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * HELPER FUNCTION
     * gets the Coordinate in front (relative to the direction the pawn is moving)
     * one and left one of the pawns current position
     * returns null if that Coordinate is not on the board
     * 
     * @return returns a Coordinate
     */
    private Coordinate leftCapture() {
        int file = position.getFileIndex() - 1;
        int advance = color == Team.WHITE ? 1 : -1;
        int rank = position.getRankIndex() + advance;
        return Coordinate.getCoordinate(file, rank);
    }

    /**
     * HELPER FUNCTION
     * gets the Coordinate in front one (relative to the direction the pawn is
     * moving) and right one of the pawns current position
     * returns null if that Coordinate is not on the board
     * 
     * @return returns a Coordinate
     */
    private Coordinate rightCapture() {
        int file = position.getFileIndex() + 1;
        int advance = color == Team.WHITE ? 1 : -1;
        int rank = position.getRankIndex() + advance;
        return Coordinate.getCoordinate(file, rank);
    }

    public boolean getUnmoved() {
        return this.Unmoved;
    }

    public void setUnmovedFalse() {
        this.Unmoved = false;

    }

}