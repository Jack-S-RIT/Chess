package unit;

import java.util.TreeMap;

/**
 * class that represents the coordinates on a chess board
 * 
 * @author Jackson Shortell
 */
public class Coordinate implements Comparable<Coordinate> {

    private static final TreeMap<Integer, Coordinate> ID_TO_COORDINATE;

    /**
     * the file of the Coordinate for horizontal position
     */
    private final File file;

    /**
     * the rank of the Coordinate for vertical position
     */
    private final Rank rank;

    /**
     * the color of the Square the Coordinate is paired with
     */
    private final Team color;

    /**
     * number to uniquely identify a Coordinate
     */
    private final int id;

    private Coordinate(File file, Rank rank, Team color, int id) {
        this.file = file;
        this.rank = rank;
        this.color = color;
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public Rank getRank() {
        return rank;
    }

    public Team getColor() {
        return color;
    }

    /**
     * gets the index of the File enum of the Coordinate
     * 
     * @return returns an int
     */
    public int getFileIndex() {
        return file.getIndex();
    }

    /**
     * gets the index of the Rank enum of the Coordinate
     * 
     * @return returns an int
     */
    public int getRankIndex() {
        return rank.getIndex();
    }

    /**
     * gets a Coordinate given the File index and Rank index
     * 
     * @param file passes in an int
     * @param rank passes in an int
     * @return returns a Coordinate
     */
    public static Coordinate getCoordinate(int file, int rank) {
        if ((-1 < file && file < 8) && (-1 < rank && rank < 8)) {
            return getCoordinate(File.indexToFile(file) , Rank.indexToRank(rank));
        } else {
            return null;
        }

    }

    /**
     * gets a Coordinate given the File enum and Rank enum
     * 
     * @param file passes in a File
     * @param rank passes in a Rank
     * @return returns a Coordinate
     */
    public static Coordinate getCoordinate(File file, Rank rank) {
        if (file != null && rank != null) {
            int fileIndex = file.getIndex();
            int rankIndex = rank.getIndex();
            return new Coordinate(file, rank, indexToTeam(fileIndex, rankIndex), (rankIndex * 8 + fileIndex) );
        } else {
            return null;
        }

    }

    /**
     * gets a Coordinate given the File index and Rank enum
     * 
     * @param file passes in an int
     * @param rank passes in a Rank
     * @return returns a Coordinate
     */
    public static Coordinate getCoordinate(int file, Rank rank) {
        if (rank != null) {
            return getCoordinate(File.indexToFile(file), rank);
        } else {
            return null;
        }
    }

    /**
     * given the rank and file calculates the coordinates color
     * @param fileIndex
     * @param rankIndex
     * @return
     */
    private static Team indexToTeam(int fileIndex, int rankIndex) {
        if (fileIndex % 2 == 0) {
            return rankIndex % 2 == 0 ? Team.BLACK : Team.WHITE;
        } else {
            return rankIndex % 2 == 0 ? Team.WHITE : Team.BLACK;
        }
    }

    @Override
    public String toString() {
        return file.toString() + rank.toString();
    }

    /**
     * compares Coordinates by their id the lower the id the higher the priority
     * sorts by rank first then by file
     */
    @Override
    public int compareTo(Coordinate other) {
        return this.id - other.id;
    }

    /**
     * creates every possible Coordinate on a chess board in order of priority
     * and adds them to a TreeMap
     */
    static {
        ID_TO_COORDINATE = new TreeMap<>();
        int key = 0;
        for (int rankIndex = 0; rankIndex < 8; rankIndex++) {
            for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
                File file = File.indexToFile(fileIndex);
                Rank rank = Rank.indexToRank(rankIndex);
                Team color = indexToTeam(fileIndex, rankIndex);
                Coordinate value = new Coordinate(file, rank, color, key);
                ID_TO_COORDINATE.put(key, value);
                key++;
            }
        }
    }

    public static void main(String[] args) {
        for (int rankIndex = 0; rankIndex < 8; rankIndex++) {
            for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
                Coordinate coord = getCoordinate(fileIndex, rankIndex);
                Team color = coord.getColor();
                System.out.println(coord + ": " + color);
            }
        }

    }

}