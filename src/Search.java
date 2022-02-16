

import java.util.*;
import java.lang.Math;
import java.io.*;


public class Search {
    // This class holds search algorithms for the board, in particularly, the A* Search Algorithm

    Board gameBoard;
    Agent agent;
    int heuristic;
    PriorityQueue<State> StateComparator = new PriorityQueue<State>(50, new Comparator<State>() {
        @Override
        public int compare(State s1, State s2) {return s1.getPriorityValue() - s2.getPriorityValue();}
    });

    // Constructor for the Search class
    public Search(Board gameBoard, Agent agent, int heuristic) {
        this.gameBoard = gameBoard;
        this.agent = agent;
        this.heuristic = heuristic;
    }

    // This method searches the board using the A* Algorithm
    public void A_Star_Search() {
        // -------------------- INITIALIZATION ------------------
        // Initializes important counters and data structures
        int numNodesExpanded = 0;
        int numActions = 0;
        int score = 0;

        // Feature extraction variables
        List<Integer> costToGoalList = new ArrayList<Integer>();
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        List<String> NewMoveList = new ArrayList<>();
        List<Integer> directionList = new ArrayList<Integer>();
        List<State> stateList = new ArrayList<State>();

        // AStar Algorithm variables
        List<String> MoveList = new ArrayList<>();
        PriorityQueue<State> OPEN = new PriorityQueue<State>(StateComparator);
        Heuristics myHeuristic = new Heuristics(this.heuristic);


        // initialize the cost_so_far matrix
        int rows = this.gameBoard.numRows;
        int columns = this.gameBoard.numCols;
        State[][][] Best_States = new State[rows][columns][4];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                for(int k = 0; k < 4; k++) {
                    Coordinate currCoord = new Coordinate(i, j);
                    Best_States[i][j][k] = new State(currCoord, k, -1);
                }
            }
        }


        // ---------- PREPPING THE START STATE ----------------
        // Retrieve and prep the start state of the agent
        Coordinate startPoint = this.gameBoard.getStartPoint();
        Coordinate endPoint = this.gameBoard.getEndPoint();


        State start_state = new State(startPoint, State.NORTH);
        start_state.previousState = null; // Set previous node of start to null
        start_state.currentCost = 0; // current path cost of start to 0
        start_state.previousMove = null; // and the previous move to null
        Best_States[start_state.getX()][start_state.getY()][State.NORTH-1] = start_state;


        // ------------ A* Search -------------------------
        // Adds start_point to priority queue
        OPEN.add(start_state);

        // while there are still states to be visited ...
        while(OPEN.size() != 0) {
            // Look at state with most priority
            State current = OPEN.remove();

            // If goal is reached, return path
            if(this.gameBoard.getComplexity(current.getX(), current.getY()) == 'G') {

                score = 100 - current.currentCost;

                // actual cost goal state
                int actualCost = current.currentCost;

                // determine series of actions in optimal path
                while(!Objects.isNull(current.previousMove)) {
                    MoveList.add(current.previousMove);

                    // add data to feature extraction variables
                    NewMoveList.add(current.previousMove);
                    coordinateList.add(current.getCoordinate());
                    costToGoalList.add(actualCost - current.currentCost);
                    directionList.add(current.getFaceDirection());
                    stateList.add(current);

                    // get previous state
                    State temp = current.previousState;
                    current = temp;

                }
                // include start state data
                NewMoveList.add("S");
                coordinateList.add(current.getCoordinate());
                costToGoalList.add(actualCost - current.currentCost);
                directionList.add(current.getFaceDirection());
                stateList.add(start_state);


                // reverse lists to obtain chronological order
                Collections.reverse(MoveList);
                Collections.reverse(costToGoalList);
                Collections.reverse(coordinateList);
                Collections.reverse(NewMoveList);
                Collections.reverse(directionList);
                Collections.reverse(stateList);

                break;
            }

            // Get the next states of the current state
            List<State> NextState = agent.getNextStates(current);

            // increment the number of nodes expanded
            numNodesExpanded += NextState.size();

            // for each next_state
            for(int i = 0; i < NextState.size(); i++) {

                // if the next state has NOT been visited yet ...
                if(Best_States[NextState.get(i).getX()]
                        [NextState.get(i).getY()]
                        [NextState.get(i).getFaceDirection()-1].getCurrentCost() == -1) {
                    // determine the priority of the new state
                    NextState.get(i).priorityValue = NextState.get(i).currentCost + myHeuristic.heuristicFunction(this.heuristic, NextState.get(i).getCoordinate(), endPoint);

                    // Put the next state into the cost_so_far matrix
                    Best_States[NextState.get(i).getX()][NextState.get(i).getY()][NextState.get(i).getFaceDirection()-1] = NextState.get(i);

                    // Add new state to the priority queue
                    OPEN.add(NextState.get(i));
                }
                // if new cost is smaller than the cost so far ...
                else if (Best_States[NextState.get(i).getX()]
                        [NextState.get(i).getY()]
                        [NextState.get(i).getFaceDirection()-1].getCurrentCost() > NextState.get(i).currentCost) {
                    // determine the priority of the new state
                    NextState.get(i).priorityValue = NextState.get(i).currentCost + myHeuristic.heuristicFunction(this.heuristic, NextState.get(i).getCoordinate(), endPoint);
                    // set old worse node to now be the new, better node
                    Best_States[current.getX()][current.getY()][current.getFaceDirection()-1] = NextState.get(i);
                    // Add new state to the priority queue
                    OPEN.add(NextState.get(i));
                }

            }

        } // end of the while loop


//        // ---------- CALCULATION OF THE OPTIMAL PATH -----------
        // Determine the number of actions in optimal path
        numActions = MoveList.size();;
        // print out path score, number of actions, and number of nodes expanded
        System.out.println("Score of the path: " + score);
        System.out.println("Number of actions: " + numActions);
        System.out.println("Number of nodes expanded: " + numNodesExpanded);
        // print out list of moves to get to goal
        for(int j = 0; j < MoveList.size(); j++) {
            System.out.println(MoveList.get(j));
        }

        // Print ML Learning Stuff
        printFeatures(NewMoveList,
                        costToGoalList,
                        coordinateList,
                        directionList,
                        stateList,
                        this.gameBoard,
                        this.agent);








    } // End of A_Star_Search()


    public void printFeatures(List<String> moveList,
                              List<Integer> actualCost,
                              List<Coordinate> coordinateList,
                              List<Integer> directionList,
                              List<State> stateList,
                              Board gameBoard,
                              Agent agent) {
        /*
            - The value of the squares immediately adjacent to the robot (8 squares)
            - The value of the squares around the goal (8 squares)
            - The robot’s location and facing direction
            - The goal location
         */
        System.out.println("\nFeatures for Machine Learning");
        Coordinate endPoint = gameBoard.getEndPoint();
        try {
            // append parameter is used to append to existing csv files
            FileWriter file = new FileWriter("astarResults1.csv", true);
            PrintWriter write = new PrintWriter(file);

            for(int i = 0; i < moveList.size(); i++) {
                // get moves
                if(moveList.get(i).equals("S")) {
                    write.print(moveList.get(i));
                }
                else {
                    write.print("after " + moveList.get(i));
                }
                write.print("\t");
                // get x distance to goal
                int xDistance = Math.abs(coordinateList.get(i).getX() - endPoint.getX());
                write.print(xDistance);
                write.print("\t");
                // get y distance to goal
                int yDistance = Math.abs(coordinateList.get(i).getY() - endPoint.getY());
                write.print(yDistance);
                write.print("\t");
                // get x distance to goal squared
                double xDistSquared = Math.pow(xDistance, 2.0);
                write.print(xDistSquared);
                write.print("\t");
                // get y distance to goal squared
                double yDistSquared = Math.pow(yDistance, 2.0);
                write.print(yDistSquared);
                write.print("\t");
                // get facing direction
                write.print(directionList.get(i));
                write.print("\t");
                // get forward complexity
                Coordinate forward = agent.getForwardSpace(stateList.get(i), 1);
                if(agent.checkSpace(forward)) {
                    char forwardComplexity = gameBoard.getComplexity(forward.getX(), forward.getY());
                    if((forwardComplexity == 'G') || (forwardComplexity == 'S')) {
                        write.print(1);
                    }
                    else {
                        write.print(Character.valueOf(forwardComplexity));
                    }
                }
                else { // if boundary
                    write.print(100);
                }
                write.print("\t");
                // get left complexity
                Coordinate left = agent.getLeftSpace(stateList.get(i));
                if(agent.checkSpace(left)) {
                    char leftComplexity = gameBoard.getComplexity(left.getX(), left.getY());
                    if((leftComplexity == 'G') || (leftComplexity == 'S')) {
                        write.print(1);
                    }
                    else {
                        write.print(Character.valueOf(leftComplexity));
                    }

                }
                else { // if boundary
                    write.print(100);
                }
                write.print("\t");

                // get right complexity
                Coordinate right = agent.getRightSpace(stateList.get(i));
                if(agent.checkSpace(right)) {
                    char rightComplexity = gameBoard.getComplexity(right.getX(), right.getY());
                    if((rightComplexity == 'G') || (rightComplexity == 'S')) {
                        write.print(1);
                    }
                    else {
                        write.print(Character.valueOf(rightComplexity));
                    }
                }
                else { // if boundary
                    write.print(100);
                }
                write.print("\t");

                // get actual cost at particular position
                write.print(actualCost.get(i));

                write.print("\n");
            }
            write.close();
        } catch (IOException exe) {
            System.out.println("Cannot create file");
        }
    }
}
