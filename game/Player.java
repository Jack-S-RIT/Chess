package game;

import java.util.TreeSet;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import unit.Team;
import unit.Coordinate;
import unit.File;
import unit.Rank;

/**
 * class that represents a player of a certain team in chess
 * 
 *  @author Jackson Shortell
 */
public class Player {

    /**
     * the Team of the Player and their Pieces
     */
    private final Team color;
    /**
     * the Players King
     */
    private King king;
    /**
     * a list of the Pieces the Player starts out with 1 Queen, 2 Rooks, 2 Knights,
     * 2 Bishops, 8 Pawns
     */
    private TreeSet<Piece> playersPieces;

    public Player(Team color) {
        this.color = color;
        Rank rank = color == Team.WHITE ? Rank.ONE : Rank.EIGHT;
        this.king = new King(Coordinate.getCoordinate(File.E, rank), color);
        playersPieces = new TreeSet<>();
        setPiecesInPlay();
    }

    /**
     * copy constructor
     * 
     * @param template passes in a Player to make a deep copy of
     */
    public Player(Player template) {
        this.color = template.getColor();
        this.king = new King(template.getKing());
        this.playersPieces = new TreeSet<>();

        TreeSet<Piece> original = template.getPlayersPieces();
        for (Piece piece : original) {
            playersPieces.add(Piece.copyPiece(piece));
        }
    }

    public Team getColor() {
        return color;
    }

    public TreeSet<Piece> getPlayersPieces() {
        return playersPieces;
    }

    public King getKing() {
        return king;
    }

    /**
     * adds a Piece to the Players list of Pieces
     * 
     * @param piece passes in a Piece to add
     */
    public void addPlayersPiece(Piece piece) {
        playersPieces.add(piece);
    }

    /**
     * removes a Piece from the Players list of pieces
     * 
     * @param piece passes in a Piece to remove
     */
    public void removePlayersPiece(Piece piece) {
        playersPieces.remove(piece);
    }

    /**
     * adds all of the players starting Pieces to their list at their starting
     * squares
     */
    private void setPiecesInPlay() {
        Rank pawnStartRank = color == Team.WHITE ? Rank.TWO : Rank.SEVEN;
        Rank pieceStartRank = color == Team.WHITE ? Rank.ONE : Rank.EIGHT;

        playersPieces.add(new Queen(Coordinate.getCoordinate(File.D, pieceStartRank), color));

        playersPieces.add(new Rook(Coordinate.getCoordinate(File.A, pieceStartRank), color));
        playersPieces.add(new Rook(Coordinate.getCoordinate(File.H, pieceStartRank), color));

        playersPieces.add(new Knight(Coordinate.getCoordinate(File.B, pieceStartRank), color));
        playersPieces.add(new Knight(Coordinate.getCoordinate(File.G, pieceStartRank), color));

        playersPieces.add(new Bishop(Coordinate.getCoordinate(File.C, pieceStartRank), color));
        playersPieces.add(new Bishop(Coordinate.getCoordinate(File.F, pieceStartRank), color));

        for (int file = 0; file < 8; file++) {
            Coordinate position = Coordinate.getCoordinate(file, pawnStartRank);
            playersPieces.add(new Pawn(position, color));
        }

    }

    @Override
    public String toString() {
        return color.toString();
    }

}