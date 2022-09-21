package game;

import pieces.Piece;
import unit.Coordinate;
import unit.Move;
import unit.Square;

/**
 * class that represents the board in chess
 * 
 *  @author Jackson Shortell
 */
public class Board {

    public static final int SIZE = 8;
    private Square[][] squareTwoDArray;
    private Move lastMove;

    public Board() {
        this.squareTwoDArray = new Square[SIZE][SIZE];
        makeBoard();
        this.lastMove = new Move(null, null);
    }

    /**
     * copy constructor
     * 
     * @param template passes in a board to make a deep copy of
     */
    public Board(Board template) {
        this.squareTwoDArray = new Square[SIZE][SIZE];
        Square[][] original = template.getSquareTwoDArray();
        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                Square square = original[rank][file];
                squareTwoDArray[rank][file] = new Square(square);
            }
        }
        this.lastMove = new Move(template.getLastMove());
    }

    /**
     * constructor helper function that initializes the 2-D array
     */
    private void makeBoard() {
        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                Square square = new Square();
                squareTwoDArray[rank][file] = square;
            }
        }
    }

    private Square[][] getSquareTwoDArray() {
        return squareTwoDArray;
    }

    public Move getLastMove() {
        return lastMove;
    }

    /**
     * sets the last moves made from and to coordinates
     * 
     * @param from passes in a Coordinate object
     * @param to   passes in a Coordinate object
     */
    public void setLastMove(Coordinate from, Coordinate to) {
        lastMove.setFrom(from);
        lastMove.setTo(to);
    }

    public void setLastMove(Move move) {
        this.lastMove = move;
    }

    /**
     * given a coordinate retrieves the square at the corresponding file and rank
     * 
     * @param coordinate passes in Coordinate object
     * @return returns a Square object
     */
    public Square getSquare(Coordinate coordinate) {
        int fileIndex = coordinate.getFile().getIndex();
        int rankIndex = coordinate.getRank().getIndex();
        return squareTwoDArray[rankIndex][fileIndex];
    }

    /**
     * moves a Piece from one Coordinate to another
     * 
     * @param from passes in the Coordinate the Piece is currently at
     * @param to   passes in the Coordinate the piece is traveling to
     * @return the Piece that was Captured
     */
    public Piece movePiece(Coordinate from, Coordinate to) {
        Piece piece = getSquare(from).getPiece();
        if (piece != null) {
            Square position = getSquare(from);
            Square destination = getSquare(to);
            piece.setPosition(to);
            return destination.setPiece(position.removePiece());
        }
        return null;
    }

    /**
     * executes a given move using the from and to coordinate
     * @param move passes in a Move object
     * @return returns the Piece that was captured
     */
    public Piece movePiece(Move move) {
        return movePiece(move.getFrom(), move.getTo());
    }

    @Override
    public String toString() {
        String s = "";
        int i = 8;
        for (int rank = SIZE - 1; rank > -1; rank--) {
            s += i + " ";
            for (int file = 0; file < SIZE; file++) {
                s += squareTwoDArray[rank][file];
            }
            s += "\n";
            i--;
        }
        s += "   A  B  C  D  E  F  G  H";
        return s;
    }

}