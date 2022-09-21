package unit;

/**
 * class that represents a move in chess
 * 
 * @author Jackson Shortell
 */
public class Move {

    /**
     * Coordinate of the Square the Piece is moving from
     */
    private Coordinate from;
    /**
     * Coordinate of the Square the Piece is moving to
     */
    private Coordinate to;

    public Move(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * copy constructor
     */
    public Move(Move template) {
        this.from = template.getFrom();
        this.to = template.getTo();
    }

    public Coordinate getFrom() {
        return this.from;
    }

    public Coordinate getTo() {
        return this.to;
    }

    public void setFrom(Coordinate from) {
        this.from = from;

    }

    public void setTo(Coordinate to) {
        this.to = to;
    }

    public boolean isFromNull() {
        return this.from == null;
    }

    public boolean isToNull() {
        return this.to == null;
    }

    /**
     * checks that the from Coordinate is NOT null AND that the to Coordinate is NOT null
     * @return returns true if the above expression evaluates to true and false otherwise
     */
    public boolean isFromAndToNotNull() {
        return !isFromNull() && !isToNull();
    }

    @Override
    public String toString() {
        return "from " + from + " to " + to;
    }

}