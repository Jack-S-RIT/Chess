package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;

/**
 * parent class of all the Pieces common behavior
 * 
 * @author Jackson Shortell
 */
public abstract class Piece implements Comparable<Piece> {

    /**
     * the point value of the piece as an int
     */
    protected final int value;
    /**
     * current position of the piece as a Coordinate
     */
    protected Coordinate position;
    /**
     * Color of the piece
     */
    protected final Team color;

    /**
     * String name of the piece
     */
    protected final String name;

    public Piece(int value, Coordinate position, Team color, String name) {
        this.value = value;
        this.position = position;
        this.color = color;
        this.name = name;
    }

    /**
     * passes in a Piece and and determines which copy constructor to use and makes
     * a deep copy of the Piece
     * 
     * @param piece passes in a Piece to be copied
     * @return returns the copied Piece
     */
    public static Piece copyPiece(Piece piece) {
        if (piece != null) {
            switch (piece.getName()) {
                case "King":
                    King king = (King) piece;
                    return new King(king);

                case "Queen":
                    Queen queen = (Queen) piece;
                    return new Queen(queen);

                case "Rook":
                    Rook rook = (Rook) piece;
                    return new Rook(rook);

                case "Knight":
                    Knight knight = (Knight) piece;
                    return new Knight(knight);

                case "Bishop":
                    Bishop bishop = (Bishop) piece;
                    return new Bishop(bishop);

                case "Pawn":
                    Pawn pawn = (Pawn) piece;
                    return new Pawn(pawn);
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Team getTeam() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    /**
     * returns the symbol of the piece with the ansi code of its team color
     * @return returns a string
     */
    public String symbol() {
        return color.getAnsi() + name.charAt(0) + "\u001B[37m";
    }

    @Override
    public String toString() {
        return color + " " + name + " on " + position;
    }

    /**
     * gets the all the valid Coordinates a piece can move to given the current
     * state of the Board
     * 
     * @param board passes in the current state of the Board
     * @return returns a TreeSet of valid Coordinates the Piece can move to
     */
    public abstract TreeSet<Coordinate> allValidMoves(Board board);

    /**
     * gets all the valid Coordinates a piece defends given the current state of the
     * board
     * 
     * @param board passes in the current state of the Board
     * @return returns a TreeSet of valid Coordinates the piece defends
     */
    public abstract TreeSet<Coordinate> allValidDefendedSquares(Board board);

    @Override
    public int compareTo(Piece o) {
        if (this.value != o.value) {
            return o.value - this.value;
        } else {
            return this.position.compareTo(o.position);
        }
    }

}