import java.io.FileNotFoundException;
import java.io.IOException;

public class Program {


    // Main method to run the program
    public static void main(String[] args) throws IOException {

        // Assignment 1: command line code
//        Board gameboard = new Board(args[0]);
//        gameboard.generateBoard();
//        Agent agent1 = new Agent(gameboard);
//        Search search1 = new Search(gameboard, agent1, Integer.parseInt(args[1]));
//        search1.A_Star_Search();

        // Assignment 3: manual input code
        for(int i = 1; i <= 6000; i++) {
            BoardGenerator genBoard = new BoardGenerator();
            Board gameboard = genBoard.generateRandom();
            Agent agent1 = new Agent(gameboard);
            Search search1 = new Search(gameboard, agent1, Integer.parseInt("5"));
            search1.A_Star_Search();
        }





    }


}
