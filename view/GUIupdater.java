package view;

import game.Board;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
// import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.Chess;
import model.ChessObserver;
import pieces.Piece;
import unit.Coordinate;
import unit.Square;

public class GUIupdater implements ChessObserver {

    private ChessGUI gui;

    public GUIupdater(ChessGUI gui) {
        this.gui = gui;
    }

    private Color coordinateToColor(Coordinate coordinate) {
        int fileIndex = coordinate.getFileIndex();
        int rankIndex = coordinate.getRankIndex();

        if (fileIndex % 2 == 0) {
            return rankIndex % 2 == 0 ? ChessGUI.BLACK : ChessGUI.WHITE;
        } else {
            return rankIndex % 2 == 0 ? ChessGUI.WHITE : ChessGUI.BLACK;
        }
    }

    @Override
    public void update(Chess model) {

        // Move move = model.getMove();
        // Coordinate from = move.getFrom();
        // Coordinate to = move.getTo();
        // Piece piece = model.getBoard().getSquare(to).getPiece();
        // Color fromColor = coordinateToColor(from);
        // Color toColor = coordinateToColor(to);
        // Image image = ChessGUI.pieceToImage(piece);

        // Button fromButton = gui.getGridPaneChild(from);
        // StackPane fromPane = ChessGUI.makeStackPane(fromColor);
        // fromButton.setGraphic(fromPane);

        // Button toButton = gui.getGridPaneChild(to);
        // StackPane toPane = ChessGUI.makeStackPane(toColor);
        // ChessGUI.addImageToStackPane(toPane, image);
        // toButton.setGraphic(toPane);

        model.setMoveNull();

        //GridPane grid = gui.getGrid();
        Board board = model.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate coord = Coordinate.getCoordinate(j, i);
                Button button = gui.getGridPaneChild(coord);
                Square square = board.getSquare(coord);
                Color color = coordinateToColor(coord);
                StackPane pane = ChessGUI.makeStackPane(color);
                Piece piece = square.getPiece();
                if (piece != null) {
                    Image image = ChessGUI.pieceToImage(piece);
                    ChessGUI.addImageToStackPane(pane, image);
                }
                button.setGraphic(pane);

            }
        }
        // System.out.println(model);
    }

}
