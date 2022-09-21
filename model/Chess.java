package model;

import java.util.TreeSet;

import game.Board;
import game.Player;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import pieces.Unmoved;
import unit.Team;
import unit.Coordinate;
import unit.File;
import unit.Move;
import unit.Rank;
import unit.Square;

/**
 * chess class which acts as the model
 * 
 *  @author Jackson Shortell
 */
public class Chess {

    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Move move;
    private Team turn;
    private Pawn toBePromoted;
    private ChessObserver observer;

    public Chess() {
        this.board = new Board();
        this.whitePlayer = new Player(Team.WHITE);
        this.blackPlayer = new Player(Team.BLACK);
        this.move = new Move(null, null);
        this.turn = Team.WHITE;
        this.toBePromoted = null;
        placePieces();
    }

    /**
     * copy constructor
     * 
     * @param template passes in an instance of Chess to make a deep copy of
     */
    public Chess(Chess template) {
        this.board = new Board(template.getBoard());
        this.whitePlayer = new Player(template.getWhitePlayer());
        this.blackPlayer = new Player(template.getBlackPlayer());
        this.move = new Move(template.getMove());
        this.turn = template.getTurn();
        Pawn templatePawn = template.getToBePromoted();
        if (templatePawn != null) {
            this.toBePromoted = (Pawn) Piece.copyPiece(templatePawn);
        } else {
            this.toBePromoted = null;
        }
    }

    public Board getBoard() {
        return board;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Move getMove() {
        return move;
    }

    public Team getTurn() {
        return turn;
    }

    public Pawn getToBePromoted() {
        return toBePromoted;
    }

    public void setToBePromoted(Pawn toBePromoted) {
        this.toBePromoted = toBePromoted;
    }

    /*
     * sets the move from and to coordinates to null
     */
    public void setMoveNull() {
        move.setFrom(null);
        move.setTo(null);
    }

    /**
     * registers an observer to observe the model
     * 
     * @param observer passes in an observer
     */
    public void register(ChessObserver observer) {
        this.observer = observer;
    }

    /**
     * notifies the observer when a change has been made to the model
     */
    private void notifyObserver() {
        if (observer != null) {
            observer.update(this);
        }

    }

    /**
     * plays a move if the to and from coords from the move field are not null
     * 
     * @return returns true if the move is executed and false if not
     */
    public boolean playMove() {
        if (move.isFromAndToNotNull()) {
            Piece movingPiece = board.getSquare(move.getFrom()).getPiece();
            Player mover = turn == Team.WHITE ? whitePlayer : blackPlayer;
            Player enemy = turn == Team.WHITE ? blackPlayer : whitePlayer;
            Piece captured = preCheckEvaluation(movingPiece, mover, enemy);
            boolean isMoverInCheck = checkEvaluation(mover);
            if (isMoverInCheck) {
                revertMove(enemy, captured);
                return false;
            }
            postCheckEvaluation(movingPiece);
            notifyObserver();
            return true;
        }
        return false;
    }

    /**
     * evaluates things that need to happen before check is evaluated such ass extra
     * pawn conditions and captures
     * 
     * @param movingPiece passes in the moving Piece object
     * @param mover       passes in which player object is moving that Piece
     * @param enemy       passes in the enemy player object
     * @return returns the captured Piece
     */
    public Piece preCheckEvaluation(Piece movingPiece, Player mover, Player enemy) {
        if (movingPiece instanceof Pawn) {
            Pawn pawn = (Pawn) movingPiece;
            extraPawnConditions(pawn, move.getTo());
        }

        Piece captured = board.movePiece(move);

        if (captured != null) {
            enemy.removePlayersPiece(captured);
        }

        return captured;
    }

    /**
     * evaluates if the king is now in check after the move has been made
     * 
     * @param mover passes in the player object that is making the move
     * @return returns true if the king is in check and false otherwise
     */
    public boolean checkEvaluation(Player mover) {
        clearSquaresDefendedBy();
        updateAllSquaresDefendedBy();
        King king = mover.getKing();
        king.updateInCheck(board);
        return king.getInCheck();
    }

    /**
     * evaluates things that need to come after the check evaluation such as if the
     * last move made was a castle and executes it or if the piece moved implements
     * unmoved and needs its unmoved status set to false
     * 
     * @param movingPiece passes in the moving piece
     */
    public void postCheckEvaluation(Piece movingPiece) {
        if (movingPiece.getName().equals("King") && isCastleMove(move)) {
            castleRook(move.getTo());
        }

        if (movingPiece instanceof Unmoved) {
            Unmoved unmoved = (Unmoved) movingPiece;
            unmoved.setUnmovedFalse();
        }

        board.setLastMove(move);
        turn = turn.opposite();
    }

    /**
     * submits a coordinate to be set as a from or a to coordinate
     * 
     * @param coordinate passes in a coordinate object
     * @return returns true if the coordinate was set and false other wise
     */
    public boolean submitCoordinate(Coordinate coordinate) {
        if (isFromCoordinate(coordinate)) {
            move.setFrom(coordinate);
            return true;
        } else if (isToCoordinate(coordinate)) {
            move.setTo(coordinate);
            return true;
        } else {
            return false;
        }
    }

    /**
     * determines if the coordinate passed in was a from coordinate or not
     * 
     * @param coordinate passes in a coordinate object
     * @return returns true if it was a from coordinate and false otherwise
     */
    public boolean isFromCoordinate(Coordinate coordinate) {
        Square square = board.getSquare(coordinate);
        if ((square.isOccupied() && square.getPieceColor() == turn)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * determines if the coordinate passed in was a to coordinate or not
     * 
     * @param coordinate passes in a coordinate object
     * @return returns true if it was a from coordinate and false otherwise
     */
    public boolean isToCoordinate(Coordinate coordinate) {
        if (!move.isFromNull()) {
            Square square = board.getSquare(move.getFrom());
            Piece piece = square.getPiece();
            if (piece.allValidMoves(board).contains(coordinate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * clears all squares defended by sets in the 2-D array of the board
     */
    public void clearSquaresDefendedBy() {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Square square = board.getSquare(Coordinate.getCoordinate(file, rank));
                square.clearDefendBy();
            }
        }
    }

    /**
     * given a player and the players information about the locations of its pieces,
     * calculates which squares are defended and updates each one
     * 
     * @param player passes in a Player object
     */
    public void updatePlayersDefendedSquares(Player player) {
        Team color = player.getColor();
        King king = player.getKing();
        TreeSet<Coordinate> defended = king.allValidDefendedSquares(board);

        for (Coordinate coordinate : defended) {
            Square square = board.getSquare(coordinate);
            square.addDefendedBy(color);
        }

        TreeSet<Piece> pieces = player.getPlayersPieces();
        for (Piece piece : pieces) {
            defended = piece.allValidDefendedSquares(board);
            for (Coordinate coordinate : defended) {
                Square square = board.getSquare(coordinate);
                square.addDefendedBy(color);
            }
        }
    }

    /**
     * calls updatePlayersDefendedSquares with the white and the black player to
     * update the whole board
     */
    public void updateAllSquaresDefendedBy() {
        updatePlayersDefendedSquares(whitePlayer);
        updatePlayersDefendedSquares(blackPlayer);
    }

    /**
     * calculates given a move if the displacement of the files is that seen in a
     * castling move called only when the king moves as it can not move more then 1
     * file in any direction so moving 2 files tell us it made a castle move
     * 
     * @param move passes in move object
     * @return returns true if it was a castle move and false otherwise
     */
    public boolean isCastleMove(Move move) {
        File fromFile = move.getFrom().getFile();
        File toFile = move.getTo().getFile();

        return fromFile == File.E && (toFile == File.C || toFile == File.G);
    }

    /**
     * given a coordinate determines what type of castle was made and by which team
     * and relocates the rook accordingly
     * 
     * @param coordinate passes in a coordinate object
     */
    public void castleRook(Coordinate coordinate) {
        File file = coordinate.getFile();
        Rank rank = coordinate.getRank();

        Coordinate from = file == File.C ? Coordinate.getCoordinate(File.A, rank)
                : Coordinate.getCoordinate(File.H, rank);

        Coordinate to = file == File.C ? Coordinate.getCoordinate(File.D, rank)
                : Coordinate.getCoordinate(File.F, rank);

        board.movePiece(from, to);
    }

    /**
     * checks to see if the move is contained in the pawns allValidMoves set and if
     * the square is empty so we know an en passant move has occurred
     * 
     * @param pawn passes in a Pawn to check its moves
     * @param to   passes in a to Coordinate to see if it is in the pawns moves
     * @return returns true if the move is an en passant move and false otherwise
     */
    public boolean isEnPassantMove(Pawn pawn, Coordinate to) {
        Square square = board.getSquare(to);
        if (pawn.allValidDefendedSquares(board).contains(to) && square.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * given a piece (the piece a pawn was promoted into) sets the piece to place on
     * its square and the players tree set to update
     * 
     * @param piece
     * @param square
     * @param player
     */
    private void addPromotedPieceToPlayerAndSquare(Piece piece, Square square, Player player) {
        square.setPiece(piece);
        player.addPlayersPiece(piece);
    }

    /**
     * given the name of a piece determines which piece to promote a pawn into
     * 
     * @param pieceName name of piece ie "Rook", "Bishop", "Queen", "Knight" no king
     *                  or pawn
     * @return returns true if the input was valid and false otherwise
     */
    public boolean promotePawn(String pieceName) {
        Player player = turn == Team.WHITE ? whitePlayer : blackPlayer;
        Coordinate position = toBePromoted.getPosition();
        player.removePlayersPiece(toBePromoted);
        Square square = board.getSquare(toBePromoted.getPosition());
        square.removePiece();
        switch (pieceName) {
            case "Queen":
                Queen queen = new Queen(position, turn);
                addPromotedPieceToPlayerAndSquare(queen, square, player);
                return true;
            case "Rook":
                Rook rook = new Rook(position, turn);
                addPromotedPieceToPlayerAndSquare(rook, square, player);
                return true;
            case "Bishop":
                Bishop bishop = new Bishop(position, turn);
                addPromotedPieceToPlayerAndSquare(bishop, square, player);
                return true;
            case "Knight":
                Knight knight = new Knight(position, turn);
                addPromotedPieceToPlayerAndSquare(knight, square, player);
                return true;
        }
        return false;
    }

    /**
     * given a to coordinate executes the en passant move and captures the piece
     * directly behind the pawns destination
     * 
     * @param to passes in a coordinate object
     */
    private void captureEnPassant(Coordinate to) {
        File file = to.getFile();
        Rank rank = turn == Team.WHITE ? Rank.FIVE : Rank.FOUR;
        Square square = board.getSquare(Coordinate.getCoordinate(file, rank));
        Player enemy = turn == Team.WHITE ? blackPlayer : whitePlayer;
        enemy.removePlayersPiece(square.removePiece());
    }

    /**
     * determines if the pawn need to be promoted or capture a piece en passant
     * 
     * @param pawn passes in a pawn to check
     * @param to   passes in a destination coordinate to check
     */
    private void extraPawnConditions(Pawn pawn, Coordinate to) {
        if (isEnPassantMove(pawn, to)) {
            captureEnPassant(to);
        }
        Rank promotionRank = pawn.getTeam() == Team.WHITE ? Rank.EIGHT : Rank.ONE;
        if (to.getRank() == promotionRank) {
            setToBePromoted(pawn);
        }
    }

    /**
     * reverts a move just made on the board in cases where an illegal move was
     * played
     * 
     * @param enemy    passes in the enemy player
     * @param captured and the captured piece that may need to be returned if not
     *                 null
     */
    private void revertMove(Player enemy, Piece captured) {
        Coordinate from = move.getFrom();
        Coordinate to = move.getTo();
        board.movePiece(to, from);
        board.getSquare(to).setPiece(captured);
        if (captured != null) {
            enemy.addPlayersPiece(captured);
        }
        this.move.setFrom(null);
        this.move.setTo(null);
        toBePromoted = null;
        clearSquaresDefendedBy();
        updateAllSquaresDefendedBy();
    }

    /**
     * calculates if a given player is in stalemate (they have no valid moves and
     * are not in check)
     * 
     * @param player passes in a Player object to check
     * @return returns true if the player is in stale mate and false otherwise
     */
    public boolean isPlayerInStalemate(Player player) {
        TreeSet<Piece> playersPieces = player.getPlayersPieces();
        King king = player.getKing();
        king.updateInCheck(board);
        if (!king.getInCheck() && king.allValidMoves(board).size() == 0) {
            for (Piece piece : playersPieces) {
                TreeSet<Coordinate> moves = piece.allValidMoves(board);
                if (moves.size() > 0) {
                    return false;
                }

            }
            return true;
        }
        return false;
    }

    /**
     * determines if a given player is in checkmate by validating that their king is
     * in check and they have no moves to move out of check, block the check, or
     * capture the piece that is checking the king using a back tracker and playing all possible moves in the position
     * 
     * @param player passes in a player to check
     * @return returns true if player is in checkmate and false otherwise
     */
    public boolean isPlayerInCheckmate(Player player) {
        King king = player.getKing();
        king.updateInCheck(board);
        if (king.getInCheck() && king.allValidMoves(board).size() == 0) {
            TreeSet<Piece> pieces = player.getPlayersPieces();
            for (Piece piece : pieces) {
                TreeSet<Coordinate> moves = piece.allValidMoves(board);
                Coordinate from = piece.getPosition();
                for (Coordinate to : moves) {
                    Chess copy = new Chess(this);
                    submitCoordinate(from);
                    submitCoordinate(to);
                    boolean valid = copy.playMove();
                    if (valid == true) {
                        return false;
                    }
                }

            }
            return true;
        }
        return false;

    }

    /**
     * determines if either player is in checkmate
     * @return returns true if either player is in checkmate and false otherwise
     */
    public boolean isEitherPlayerInCheckmate() {
        return isPlayerInCheckmate(whitePlayer) || isPlayerInCheckmate(blackPlayer);
    }

    /**
     * determines if either player is in stalemate
     * @return returns true if either player is in stalemate and false otherwise
     */
    public boolean isEitherPlayerInStalemate() {
        return isPlayerInStalemate(whitePlayer) || isPlayerInStalemate(blackPlayer);
    }

    /**
     * places all of a players pieces on the board
     * @param player passes in a player object
     */
    private void placePlayersPieces(Player player) {
        King king = player.getKing();
        Square kingSquare = board.getSquare(king.getPosition());
        kingSquare.setPiece(king);

        TreeSet<Piece> playersPieces = player.getPlayersPieces();
        for (Piece piece : playersPieces) {
            Square square = board.getSquare(piece.getPosition());
            square.setPiece(piece);
        }
    }

    /**
     * places both players pieces
     */
    private void placePieces() {
        placePlayersPieces(whitePlayer);
        placePlayersPieces(blackPlayer);
    }

    @Override
    public String toString() {
        return board.toString();
    }

}