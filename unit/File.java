package unit;

/**
 * enum that represents the files of a chess board
 * 
 * @author Jackson Shortell
 */
public enum File {
    A('A', 0),
    B('B', 1),
    C('C', 2),
    D('D', 3),
    E('E', 4),
    F('F', 5),
    G('G', 6),
    H('H', 7);

    /**
     * char representation of the file
     */
    private char name;

    /**
     * int for indexing in an array
     */
    private int index;

    File(char name, int index) {
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
     * Given an int returns the File that matches its index,
     * if index does not match any File returns null.
     * 
     * @param i passes in an int
     * @return returns a File
     */
    public static File indexToFile(int i) {
        switch (i) {
            case 0:
                return File.A;
            case 1:
                return File.B;
            case 2:
                return File.C;
            case 3:
                return File.D;
            case 4:
                return File.E;
            case 5:
                return File.F;
            case 6:
                return File.G;
            case 7:
                return File.H;
        }
        return null;
    }

}