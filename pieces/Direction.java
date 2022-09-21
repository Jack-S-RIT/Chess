package pieces;

import java.util.TreeSet;

import game.Board;
import unit.Team;
import unit.Coordinate;
import unit.Square;

/**
 * class that calculates moves for directional pieces that have scope and can be blocked such as the bishop, rook, and queen
 * 
 *  @author Jackson Shortell
 */
public class Direction {

    /**
     * determines the valid moves of a Piece sliding in any of the 8 directions,
     * stops if the Piece reaches the end of the board OR if the Piece runs into
     * another Piece, if the stationary Piece is the same Color as the Piece moving
     * stop adding to the TreeSet and DON'T include the Coordinate of the stationary
     * Piece, if the stationary Piece is NOT the same Color as the moving Piece stop
     * adding and DO add the Coordinate of the stationary Piece
     * 
     * @param board    passes in the current state of the Board
     * @param position passes in the position of the Piece
     * @param color    passes in the Color of the Piece moving
     * @param fileAdd  passes in an int (-1, 0, 1) to indicate how much the file is
     *                 changing with each move
     * @param rankAdd  passes in an int (-1, 0, 1) to indicate how much the rank is
     *                 changing with each move
     * @return returns a TreeSet of Coordinates that are valid moves
     */
    private static TreeSet<Coordinate> slidePiece(Board board, Coordinate position, Team color, int fileAdd,
            int rankAdd) {
        TreeSet<Coordinate> moves = new TreeSet<>();
        int fileIndex = position.getFileIndex();
        int rankIndex = position.getRankIndex();
        int fileChange = fileAdd;
        int rankChange = rankAdd;

        while (true) {
            position = Coordinate.getCoordinate(fileIndex + fileChange, rankIndex + rankChange);
            if (position == null) {
                break;
            }
            Square square = board.getSquare(position);
            if (square.isOccupied()) {
                Team pieceColor = square.getPiece().getTeam();
                if (pieceColor == color) {
                    break;
                } else {
                    moves.add(position);
                    break;
                }
            }
            moves.add(position);
            fileChange += fileAdd;
            rankChange += rankAdd;
        }
        return moves;
    }

    /**
     * gets all the valid moves of sliding a Piece up, down, left, and right
     * 
     * @param board    passes in the current state of the board
     * @param position passes in the position of the piece moving
     * @param color    passes in the Color of the piece moving
     * @return returns a TreeSet of Coordinates that are valid moves
     */
    protected static TreeSet<Coordinate> validLinearMoves(Board board, Coordinate position, Team color) {
        TreeSet<Coordinate> moves = slidePiece(board, position, color, 0, 1);
        moves.addAll(slidePiece(board, position, color, 1, 0));
        moves.addAll(slidePiece(board, position, color, 0, -1));
        moves.addAll(slidePiece(board, position, color, -1, 0));
        return moves;
    }

    /**
     * gets the valid moves from 45 degree diagonal directions
     * 
     * @param board    passes in the current state of the board
     * @param position passes in the position of the Piece moving
     * @param color    passes in the Color of the Piece moving
     * @return returns a TreeSet of Coordinates that are all valid moves
     */
    protected static TreeSet<Coordinate> validDiagonalMoves(Board board, Coordinate position, Team color) {
        TreeSet<Coordinate> moves = slidePiece(board, position, color, 1, 1);
        moves.addAll(slidePiece(board, position, color, 1, -1));
        moves.addAll(slidePiece(board, position, color, -1, -1));
        moves.addAll(slidePiece(board, position, color, -1, 1));
        return moves;
    }

    /**
     * gets all the Coordinates a Piece defends in the linear directions
     * 
     * @param board    passes in the current state of the board
     * @param position passes in the position of the Piece
     * @return returns a TreeSet of Coordinates the Piece defends
     */
    protected static TreeSet<Coordinate> validLinearDefends(Board board, Coordinate position) {
        TreeSet<Coordinate> defends = validLinearMoves(board, position, Team.WHITE);
        defends.addAll(validLinearMoves(board, position, Team.BLACK));
        return defends;
    }

    /**
     * gets all the Coordinates a Piece defends in the 45 degree diagonal directions
     * 
     * @param board    passes in the current state of the board
     * @param position passes in the position of the Piece
     * @return returns a TreeSet of Coordinates the Piece defends
     */
    protected static TreeSet<Coordinate> validDiagonalDefends(Board board, Coordinate position) {
        TreeSet<Coordinate> defends = validDiagonalMoves(board, position, Team.WHITE);
        defends.addAll(validDiagonalMoves(board, position, Team.BLACK));
        return defends;
    }

}