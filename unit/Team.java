package unit;

/**
 * enum that represents the two Teams in chess
 * 
 * @author Jackson Shortell
 */
public enum Team {

    WHITE("White", "\u001b[33m"), // ansi code for yellow to represent white in the CLI
    BLACK("Black", "\u001B[34m"); // ansi code for blue to represent black in the CLI

    /**
     * String representation of the Color
     */
    private String name;
    /**
     * String for ansi code corresponding with the color
     */
    private String ansi;

    Team(String name, String ansi) {
        this.name = name;
        this.ansi = ansi;

    }

    @Override
    public String toString() {
        return name;
    }

    public String getAnsi() {
        return ansi;
    }

    /**
     * given a color returns the opposite Color
     * 
     * @return returns a Color
     */
    public Team opposite() {
        if (name.equals("White")) {
            return Team.BLACK;
        } else {
            return Team.WHITE;
        }
    }

}