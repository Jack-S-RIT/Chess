package view;

import java.util.Scanner;
import java.util.regex.Pattern;

import game.Board;
import model.Chess;
import pieces.Piece;
import unit.Coordinate;
import unit.File;
import unit.Rank;

/**
 * class the allows to play chess through a CLI and offers commands
 */
public class ChessCLI {

    private Chess game;

    public ChessCLI() {
        game = new Chess();
    }

    public Chess getGame() {
        return game;
    }

    /**
     * given a uppercase or lowercase char returns the corresponding file (done for
     * user flexibility in commands)
     *
     * @param c passes in a char
     * @return returns a file if char matches and null otherwise
     */
    private File charToFile(char c) {
        switch (c) {
            case 'a', 'A':
                return File.A;
            case 'b', 'B':
                return File.B;
            case 'c', 'C':
                return File.C;
            case 'd', 'D':
                return File.D;
            case 'e', 'E':
                return File.E;
            case 'f', 'F':
                return File.F;
            case 'g', 'G':
                return File.G;
            case 'h', 'H':
                return File.H;
        }
        return null;
    }

    /**
     * given a char returns the corresponding rank
     *
     * @param c passes in a char
     * @return returns a rank if char matches and null otherwise
     */
    private Rank charToRank(int c) {
        switch (c) {
            case '1':
                return Rank.ONE;
            case '2':
                return Rank.TWO;
            case '3':
                return Rank.THREE;
            case '4':
                return Rank.FOUR;
            case '5':
                return Rank.FIVE;
            case '6':
                return Rank.SIX;
            case '7':
                return Rank.SEVEN;
            case '8':
                return Rank.EIGHT;
        }
        return null;
    }

    /**
     * given a string such as "E4", "H8", "F7" coverts it into a coordinate object
     * 
     * @param string passes in a string
     * @return returns a coordinate object
     */
    private Coordinate stringToCoordinate(String string) {
        File file = charToFile(string.charAt(0));
        Rank rank = charToRank(string.charAt(1));
        return Coordinate.getCoordinate(file, rank);
    }

    /**
     * given an input string uses regular expression to break down and understand
     * the command to help the user, execute moves, look at possible moves, and quit
     * 
     * @param input input string
     * @return returns a string that can ID what kind of command was executed
     */
    public String readUserInput(String input) {
        if (input.equals("quit")) {
            return input;
        } else if (input.equals("help")) {

            String help = "quit - quits the game\nhelp - produces this message\n"
                    + "move <Coordinate 1> <Coordinate 2> - move a piece from Coordinate 1 to Coordinate 2 if the move is valid\n"
                    + "<Coordinate> moves - produces a list of available moves of the piece on that Coordinate\n";
            System.out.println(help);
            return input;

        } else if (Pattern.matches("(?:move )[a-hA-H][1-8][ ][a-hA-H][1-8]", input)) {
            String[] tokens = input.split(" ");
            Coordinate from = stringToCoordinate(tokens[1]);
            Coordinate to = stringToCoordinate(tokens[2]);
            game.submitCoordinate(from);
            game.submitCoordinate(to);
            boolean valid = game.playMove();
            if (valid) {
                game.setMoveNull();
                return "valid move";
            } else {
                return "invalid move";
            }

        } else if (Pattern.matches("[a-hA-H][1-8][ ](?:moves)", input)) {
            String[] tokens = input.split(" ");
            Board board = game.getBoard();
            Piece piece = board.getSquare(stringToCoordinate(tokens[0])).getPiece();
            if (piece != null) {
                System.out.println(piece.allValidMoves(board));
            } else {
                System.out.println("no piece on " + tokens[0]);
            }

        }
        return "";
    }

    /**
     * given a scanner prompts the user to enter a letter to indicate what piece they want to promote a pawn into
     * @param scanner passes in a scanner
     * @return returns a string
     */
    public String promptUserToPromote(Scanner scanner) {
        System.out.println("Promote your pawn to a queen, knight, rook, or bishop?\n"
                +
                "Enter 'Q' for queen, 'K' for knight, 'R' for rook, or 'B' for bishop.");
        return scanner.nextLine();
    }

    public static void main(String[] args) {

        ChessCLI chessCLI = new ChessCLI();
        Chess game = chessCLI.getGame();

        Scanner scanner = new Scanner(System.in);

        boolean gameIsAlive = true;
        while (gameIsAlive) {

            if (game.isEitherPlayerInCheckmate() || game.isEitherPlayerInStalemate()) {
                break;
            }
            System.out.println(game);
            System.out.println("Turn: " + game.getTurn());
            System.out.print(">> ");
            String input = scanner.nextLine();

            String evaluation = chessCLI.readUserInput(input);

            if (evaluation.equals("quit")) {
                break;
            } else if (evaluation.equals("invalid move")) {
                System.out.println(evaluation);
            }

            if (game.getToBePromoted() != null) {
                boolean pawnHasNotBeenPromoted = true;
                while (pawnHasNotBeenPromoted) {

                }
            }

        }
        scanner.close();

    }
}
