import java.io.FileNotFoundException;

public class Program {


    // Main method to run the program
    public static void main(String[] args) throws FileNotFoundException {

        // command line code
        Board gameboard = new Board(args[0]);
        gameboard.generateBoard();
        Agent agent1 = new Agent(gameboard);
        Search search1 = new Search(gameboard, agent1, Integer.parseInt(args[1]));
        search1.A_Star_Search();

        // manual input code
//        for(int i = 1; i <= 20; i++) {
//            String currentBoardNum = String.valueOf(i);
//            Board gameboard = new Board("sampleBoard" + currentBoardNum + ".txt");
//            gameboard.generateBoard();
//            Agent agent1 = new Agent(gameboard);
//            Search search1 = new Search(gameboard, agent1, Integer.parseInt("5"));
//            search1.A_Star_Search();
//        }


    }


}
