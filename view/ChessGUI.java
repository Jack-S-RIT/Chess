package view;

import game.Board;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Chess;
import pieces.Piece;
import unit.Coordinate;
import unit.Team;

public class ChessGUI extends Application {

    public final static Image WHITE_PAWN = new Image("data/White_Pawn.png");
    public final static Image BLACK_PAWN = new Image("data/Black_Pawn.png");
    public final static Image WHITE_KNIGHT = new Image("data/White_Knight.png");
    public final static Image BLACK_KNIGHT = new Image("data/BLACK_Knight.png");
    public final static Image WHITE_BISHOP = new Image("data/White_Bishop.png");
    public final static Image BLACK_BISHOP = new Image("data/Black_Bishop.png");
    public final static Image WHITE_ROOK = new Image("data/White_Rook.png");
    public final static Image BLACK_ROOK = new Image("data/Black_Rook.png");
    public final static Image WHITE_QUEEN = new Image("data/White_Queen.png");
    public final static Image BLACK_QUEEN = new Image("data/Black_Queen.png");
    public final static Image WHITE_KING = new Image("data/White_King.png");
    public final static Image BLACK_KING = new Image("data/Black_King.png");
    public final static Color BLACK = Color.rgb(106, 155, 65);
    public final static Color WHITE = Color.rgb(243, 243, 244);
    public final static Color WHITE_RED = Color.rgb(209, 109, 77);
    public final static Color BLACK_RED = Color.rgb(237, 127, 113);

    private Chess chess;
    private GridPane grid;

    public Chess getChess() {
        return chess;
    }

    public GridPane getGrid() {
        return grid;
    }

    public static Image pieceToImage(Piece piece) {
        if (piece == null) {
            return null;
        }
        String name = piece.getName();
        Team team = piece.getTeam();
        switch (name) {
            case "Pawn":
                return team == Team.WHITE ? WHITE_PAWN : BLACK_PAWN;
            case "Knight":
                return team == Team.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case "Bishop":
                return team == Team.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case "Rook":
                return team == Team.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case "Queen":
                return team == Team.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case "King":
                return team == Team.WHITE ? WHITE_KING : BLACK_KING;
            default:
                return null;
        }
    }

    public static Label makeLabel(String text, Color foreground, Color background, double height, double width) {
        Label label = new Label(text);
        label.setTextFill(foreground);
        label.setFont(new Font("Calibri", 14));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPadding(new Insets(5));
        // label.setPrefHeight(120);
        // label.setPrefWidth(120);
        label.setMaxHeight(height);
        label.setMaxWidth(width);
        label.setBackground(new Background(new BackgroundFill(background, CornerRadii.EMPTY, Insets.EMPTY)));
        return label;
    }

    public static StackPane makeStackPane(Color color) {
        StackPane pane = new StackPane();
        pane.getChildren()
                .addAll(makeLabel("", Color.TRANSPARENT, color, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        return pane;
    }

    public static void addImageToStackPane(StackPane pane, Image image) {
        ImageView view = new ImageView(image);
        pane.getChildren().addAll(view);
        pane.setMaxHeight(Double.POSITIVE_INFINITY);
        pane.setMaxWidth(Double.POSITIVE_INFINITY);
    }

    public Button makeButton(StackPane pane, Color color, Coordinate coordinate) {
        Button button = new Button();
        button.setGraphic(pane);
        button.setMinHeight(60);
        button.setMinWidth(60);
        button.setMaxHeight(60);
        button.setMaxWidth(60);
        button.setPadding(new Insets(0));
        button.setOnAction(new SquareButtonHandler(chess, coordinate));
        return button;
    }

    public GridPane makeGridPane() {
        GridPane grid = new GridPane();
        boolean isBlack = false;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Color color = isBlack ? Color.rgb(106, 155, 65) : Color.rgb(243, 243, 244);
                Coordinate coordinate = Coordinate.getCoordinate(file, 7 - rank);
                StackPane pane = makeStackPane(color);
                Board board = chess.getBoard();
                Piece piece = board.getSquare(coordinate).getPiece();
                Image image = pieceToImage(piece);
                if (image != null) {
                    addImageToStackPane(pane, image);
                }
                Button button = makeButton(pane, color, coordinate);

                grid.add(button, file, rank);
                if (file != 7) {
                    isBlack = !isBlack;
                }
            }
        }
        grid.setPadding(new Insets(2));
        grid.setMaxHeight(Double.MAX_VALUE);
        grid.setMaxWidth(Double.MAX_VALUE);
        return grid;
    }

    public Button getGridPaneChild(Coordinate coordinate) {
        int file = coordinate.getFileIndex();
        int rank = 7 - coordinate.getRankIndex();
        for (Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == rank && GridPane.getColumnIndex(node) == file) {
                return (Button) node;
            }
        }
        return null;
    }

    @Override
    public void start(Stage stage) throws Exception {
        chess = new Chess();
        GUIupdater observer = new GUIupdater(this);
        chess.register(observer);
        grid = makeGridPane();

        stage.setScene(new Scene(grid));
        stage.setTitle("Chess");
        stage.setMaxHeight(720);
        stage.setMaxWidth(720);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}