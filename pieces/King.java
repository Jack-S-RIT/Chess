package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;
import unit.File;
import unit.Rank;
import unit.Square;

/**
 * class that represents the King Piece in chess
 * 
 * @author Jackson Shortell
 */
public class King extends Piece implements Unmoved {

    private static final int VALUE = 39;
    private static final String NAME = "King";

    private final Rank pieceStart = color == Team.WHITE ? Rank.ONE : Rank.EIGHT;

    /**
     * boolean that represents if the king is in check
     */
    private boolean inCheck;
    /**
     * boolean that represents if the king has made his first move
     */
    private boolean Unmoved;

    public King(Coordinate position, Team color) {
        super(VALUE, position, color, NAME);
        this.inCheck = false;
        this.Unmoved = true;
    }

    /**
     * copy constructor
     * 
     * @param template passes in a King to make a copy of
     */
    public King(King template) {
        super(template.getValue(), template.getPosition(), template.getTeam(), template.getName());
        this.inCheck = template.getInCheck();
        this.Unmoved = template.getUnmoved();
    }

    public boolean getInCheck() {
        return inCheck;
    }

    /**
     * updates the inCheck boolean if the Square the King is on is defended by the
     * opposite color
     */
    public void updateInCheck(Board board) {
        Square square = board.getSquare(position);
        this.inCheck = square.getDefendedBy().contains(color.opposite());
    }

    @Override
    public TreeSet<Coordinate> allValidMoves(Board board) {
        TreeSet<Coordinate> possibleMoves = possibleMoves();
        TreeSet<Coordinate> validMoves = new TreeSet<>();
        for (Coordinate cord : possibleMoves) {
            Square square = board.getSquare(cord);
            if (isSquareValid(square)) {
                validMoves.add(cord);
            }
        }

        if (isKingSideCastleValid(board)) {
            Coordinate cord = Coordinate.getCoordinate(File.G, pieceStart);
            validMoves.add(cord);
        }

        if (isQueenSideCastleValid(board)) {
            Coordinate cord = Coordinate.getCoordinate(File.C, pieceStart);
            validMoves.add(cord);
        }
        return validMoves;
    }

    @Override
    public TreeSet<Coordinate> allValidDefendedSquares(Board board) {
        TreeSet<Coordinate> possibleMoves = possibleMoves();
        TreeSet<Coordinate> defends = new TreeSet<>();
        for (Coordinate cord : possibleMoves) {
            Square square = board.getSquare(cord);
            if (!square.getDefendedBy().contains(color.opposite())) {
                defends.add(cord);
            }
        }
        return defends;
    }

    /**
     * HELPER FUNCTION
     * checks if a square is valid to move to
     * 
     * @param square passes in a square to check
     * @return returns a boolean
     */
    private boolean isSquareValid(Square square) {
        if (square.getDefendedBy().contains(color.opposite())) {
            return false;
        } else {
            if (square.isOccupied()) {
                Piece piece = square.getPiece();
                return color != piece.getTeam();
            } else {
                return true;
            }
        }
    }

    /**
     * HELPER FUNCTION
     * gets a TreeSet of the possible 8 or less moves the king can make
     * 
     * @param board    passes in the current state of the board
     * @param position passes in the position of the king
     * @return returns a TreeSet containing valid moves
     */
    private TreeSet<Coordinate> possibleMoves() {
        TreeSet<Coordinate> moves = new TreeSet<>();
        int one = 1;
        for (int i = 0; i < 2; i++) {
            addCoordToTreeSet(moves, 0, one);
            addCoordToTreeSet(moves, one, one);
            addCoordToTreeSet(moves, one, 0);
            addCoordToTreeSet(moves, one, -one);
            one *= -1;
        }
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
     * produces a Coordinate that the King could possibly move to
     * 
     * @param fileChange passes in an int (-1, 0, or 1) to indicate the new file
     * @param rankChange passes in an int (-1, 0, or 1) to indicate the new rank
     * @return returns a Coordinate
     */
    private Coordinate singleMove(int fileChange, int rankChange) {
        int rankIndex = position.getRankIndex();
        int fileIndex = position.getFileIndex();
        File file = File.indexToFile(fileIndex + fileChange);
        Rank rank = Rank.indexToRank(rankIndex + rankChange);
        return Coordinate.getCoordinate(file, rank);
    }

    /**
     * helper function
     * checks to see if king side castling is a valid move
     * 
     * @param board passes in the current state of the board
     * @return returns true if it is valid, false otherwise
     */
    private boolean isKingSideCastleValid(Board board) {
        if (Unmoved && !inCheck) {
            if (isRookUnmoved(board, File.H) && isKingCastleSquaresClear(board)) {
                return true;
            }
        }
        return false;
    }

    /**
     * helper function
     * checks to see if queen side castling is a valid move
     * 
     * @param board passes in the current state of the board
     * @return returns true if it is valid, false otherwise
     */
    private boolean isQueenSideCastleValid(Board board) {
        if (Unmoved && !inCheck) {
            if (isRookUnmoved(board, File.A) && isQueenCastleSquaresClear(board)) {
                return true;
            }
        }
        return false;
    }

    /**
     * HELPER FUNCTION
     * checks if the same color Rook on a given file enum has been moved
     * 
     * @param board passes in the current state of the board
     * @param file  passes in the file of the rook your checking
     * @return returns true if the rook you checked is un moved, false otherwise
     */
    private boolean isRookUnmoved(Board board, File file) {
        Coordinate cord = Coordinate.getCoordinate(file, pieceStart);
        Piece piece = board.getSquare(cord).getPiece();
        if (piece instanceof Rook) {
            Rook rook = (Rook) piece;
            if (rook.getUnmoved() && rook.getTeam() == this.color) {
                return true;
            }

        }
        return false;
    }

    /**
     * HELPER FUNCTION
     * checks to see if the king can castle on the king side
     * 
     * @param board passes in the current state of the board
     * @return returns true if the king can castle on the king side, false otherwise
     */
    private boolean isKingCastleSquaresClear(Board board) {
        Coordinate coord = Coordinate.getCoordinate(File.F, pieceStart);
        Square fSquare = board.getSquare(coord);
        coord = Coordinate.getCoordinate(File.G, pieceStart);
        Square gSquare = board.getSquare(coord);

        return isCastleSquareValid(fSquare) && isCastleSquareValid(gSquare);
    }

    /**
     * HELPER FUNCTION
     * checks to see if the king can castle on the queen side
     * 
     * @param board passes in the current state of the board
     * @return returns true if the king can castle on the queen side, false
     *         otherwise
     */
    private boolean isQueenCastleSquaresClear(Board board) {
        Coordinate coord = Coordinate.getCoordinate(File.B, pieceStart);
        Square bSquare = board.getSquare(coord);
        coord = Coordinate.getCoordinate(File.C, pieceStart);
        Square cSquare = board.getSquare(coord);
        coord = Coordinate.getCoordinate(File.D, pieceStart);
        Square dSquare = board.getSquare(coord);

        return bSquare.isEmpty() && isCastleSquareValid(cSquare) && isCastleSquareValid(dSquare);

    }

    /**
     * HELPER FUNCTION
     * checks to see if a given Square is empty and not defended by the opposite
     * color
     * 
     * @param square passes in the Square you are checking
     * @return returns true if the Square is empty and not defended by the opposite
     *         color, false otherwise
     */
    private boolean isCastleSquareValid(Square square) {
        return square.isEmpty() && !square.getDefendedBy().contains(color.opposite());
    }

    public boolean getUnmoved() {
        return this.Unmoved;
    }

    public void setUnmovedFalse() {
        this.Unmoved = false;
    }

}