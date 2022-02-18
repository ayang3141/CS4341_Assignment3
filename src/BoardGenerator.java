import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class BoardGenerator {

    public Board generateRandom() throws IOException {


        boolean placedStart = false;
        boolean placedGoal = false;
        int numRows = 10;
        int numColumns = 10;
        Random random = new Random();

        char[][] board = new char[numRows][numColumns];
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numColumns; j++) {
                board[i][j] = (char)(random.nextInt(9) + 1 + '0');
            }
        }

        int goalX = 0, goalY = 0, startX = 0, startY = 0;
        while((goalX == startX) && (goalY == startY)) {
            goalX = random.nextInt(numRows);
            goalY = random.nextInt(numColumns);
            startX = random.nextInt(numRows);
            startY = random.nextInt(numColumns);
        }
        board[goalX][goalY] = 'G';
        board[startX][startY] = 'S';


        FileWriter file = new FileWriter("sampleBoard2.txt", false);
        PrintWriter write = new PrintWriter(file);
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numColumns; j++) {
                char value = board[i][j];
                if((value == 'S') || (value == 'G')) {
                    write.print(value);
                }
                else {
                    write.print(Character.getNumericValue(value));
                }

                if(j == numColumns - 1) {
                    write.print("\n");
                }
                else {
                    write.print("\t");
                }
            }
        }
        write.close();


        return new Board(board);
    }

    public static void main(String[] args) throws IOException {


        boolean placedStart = false;
        boolean placedGoal = false;
        int numRows = 20;
        int numColumns = 20;
        Random random = new Random();

        char[][] board = new char[numRows][numColumns];
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numColumns; j++) {
                board[i][j] = (char)(random.nextInt(9) + 1 + '0');
            }
        }

        int goalX = 0, goalY = 0, startX = 0, startY = 0;
        while((goalX == startX) && (goalY == startY)) {
            goalX = random.nextInt(numRows);
            goalY = random.nextInt(numColumns);
            startX = random.nextInt(numRows);
            startY = random.nextInt(numColumns);
        }
        board[goalX][goalY] = 'G';
        board[startX][startY] = 'S';


        FileWriter file = new FileWriter("sampleBoard10.txt", false);
        PrintWriter write = new PrintWriter(file);
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numColumns; j++) {
                char value = board[i][j];
                if((value == 'S') || (value == 'G')) {
                    write.print(value);
                }
                else {
                    write.print(Character.getNumericValue(value));
                }

                if(j == numColumns - 1) {
                    write.print("\n");
                }
                else {
                    write.print("\t");
                }
            }
        }
        write.close();

    }





}
