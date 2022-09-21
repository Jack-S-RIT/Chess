package unit;

/**
 * enum that represents the ranks of a chess board
 * 
 * @author Jackson Shortell
 */
public enum Rank {
    ONE('1', 0),
    TWO('2', 1),
    THREE('3', 2),
    FOUR('4', 3),
    FIVE('5', 4),
    SIX('6', 5),
    SEVEN('7', 6),
    EIGHT('8', 7);

    /**
     * char representation of the rank
     */
    private char name;

    /**
     * int for indexing in an array
     */
    private int index;

    Rank(char name, int index) {
        this.index = index;
        this.name = name;
    }

    public char getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name + "";
    }

    /**
     * Given an int returns the Rank that matches its index,
     * if index does not match any Rank returns null.
     * 
     * @param i passes in an int
     * @return returns a Rank
     */
    public static Rank indexToRank(int i) {
        switch (i) {
            case 0:
                return Rank.ONE;
            case 1:
                return Rank.TWO;
            case 2:
                return Rank.THREE;
            case 3:
                return Rank.FOUR;
            case 4:
                return Rank.FIVE;
            case 5:
                return Rank.SIX;
            case 6:
                return Rank.SEVEN;
            case 7:
                return Rank.EIGHT;
        }
        return null;
    }

}