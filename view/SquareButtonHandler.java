package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Chess;
import unit.Coordinate;

public class SquareButtonHandler implements EventHandler<ActionEvent> {

    private Chess chess;
    private Coordinate coordinate;

    public SquareButtonHandler(Chess chess, Coordinate coordinate) {
        this.chess = chess;
        this.coordinate = coordinate;
    }

    @Override
    public void handle(ActionEvent event) {
        chess.submitCoordinate(coordinate);

        if (chess.getMove().isFromAndToNotNull()) {
            chess.playMove();
        }
    }

}
