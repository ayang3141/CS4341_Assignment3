

import java.lang.*;

public class Heuristics {

    // This class is responsible for the heuristic functions


    public static final int H_FIRST = 1;
    public static final int H_SECOND = 2;
    public static final int H_THIRD = 3;
    public static final int H_FOURTH = 4;
    public static final int H_FIFTH = 5;
    public static final int H_SIXTH = 6;

    int heuristicChoice;

    // Constructor for the Heuristics class
    public Heuristics(int heuristicChoice) {
        this.heuristicChoice = heuristicChoice;
    }

    // calculates the value of a specified heuristic function
    public int heuristicFunction(int choice, Coordinate current, Coordinate target) {
        // Choosing heuristic function
        switch(choice)
        {
            case H_FIRST:
                return 0;
            case H_SECOND:
                return minimumHeuristic(current, target);
            case H_THIRD:
                return maximumHeuristic(current, target);
            case H_FOURTH:
                return sumHeuristic(current, target);
            case H_FIFTH:
                return admissableHeuristic(current, target);
            case H_SIXTH:
                return nonadmissableHeuristic(current, target);
        }
        return 0;
    }

    // Horizontal heuristic function
    public int findHorizontal(Coordinate current, Coordinate target) {
        return Math.abs(current.getX() - target.getX());
    }

    // Vertical heuristic function
    public int findVertical(Coordinate current, Coordinate target) {
        return Math.abs(current.getY() - target.getY());
    }

    // Minimum of horizontal and vertical heuristic function
    public int minimumHeuristic(Coordinate current, Coordinate target) {
        return Math.min(findHorizontal(current, target), findVertical(current, target));
    }

    // Maximum of horizontal and vertical heuristic function
    public int maximumHeuristic(Coordinate current, Coordinate target) {
        return Math.max(findHorizontal(current, target), findVertical(current, target));
    }

    // Sum of horizontal and vertical heuristic function
    public int sumHeuristic(Coordinate current, Coordinate target) {
        return findHorizontal(current, target) + findVertical(current, target);
    }

    // admissible heuristic function (straight line distance)
    public int admissableHeuristic(Coordinate current, Coordinate target) {
        return sumHeuristic(current, target) + 1;
    }

    // inadmissible heuristic function (3 times admissible function)
    public int nonadmissableHeuristic(Coordinate current, Coordinate target) {
        return 3 * admissableHeuristic(current, target);
    }

    public int mlHeuristic(State currentState, Agent agent, Board gameBoard) {
        // Model Coefficients
        double xDistCoef = 2.9421;
        double yDistCoef = 2.9368;
        double BothNorthCoef = -0.9144;
        double BothSouthCoef = -1.0232;
        double BothWestCoef = -1.4402;
        double BothEastCoef = -1.2751;
        double ForwardComplexityCoef = 0.0075;
        double BackwardComplexityCoef = -0.0029;

        // Calculation of feature values
        int xDist = currentState.getX() - gameBoard.getEndPoint().getX();
        int yDist = currentState.getY() - gameBoard.getEndPoint().getY();

        int G_North = (xDist >= 0) ? 1 : 0;
        int G_West = (yDist >= 0) ? 1 : 0;
        int G_South = (xDist < 0) ? 1 : 0;
        int G_East = (yDist < 0) ? 1 : 0;

        xDist = Math.abs(xDist);
        yDist = Math.abs(yDist);

        int R_North = 0;
        int R_West = 0;
        int R_South = 0;
        int R_East = 0;
        int faceDirection = currentState.getFaceDirection();
        switch(faceDirection) {
            case State.NORTH:
                R_North = 1;
                break;
            case State.WEST:
                R_West = 1;
                break;
            case State.SOUTH:
                R_South = 1;
                break;
            case State.EAST:
                R_East = 1;
                break;
        }

        Coordinate current = currentState.getCoordinate();
        int currentComplexity = 0;
        char value = gameBoard.getComplexity(current.getX(), current.getY());
        if((value == 'S') || (value == 'G')) {
            currentComplexity = 1;
        }
        else {
            currentComplexity = Character.getNumericValue(value);
        }


        Coordinate forward = agent.getForwardSpace(currentState, 1);
        int forwardComplexity = 0;
        if(!gameBoard.OutOfBounds(forward)) {
            value = gameBoard.getComplexity(forward.getX(), forward.getY());
            if((value == 'S') || (value == 'G')) {
                forwardComplexity = 1;
            }
            else {
                forwardComplexity = Character.getNumericValue(value);
            }
        }
        else {
            forwardComplexity = 100;
        }

        Coordinate backward = agent.getBackwardSpace(currentState);
        int backwardComplexity = 0;
        if(!gameBoard.OutOfBounds(backward)) {
            value = gameBoard.getComplexity(backward.getX(), backward.getY());
            if((value == 'S') || (value == 'G')) {
                backwardComplexity = 1;
            }
            else {
                backwardComplexity = Character.getNumericValue(value);
            }
        }
        else {
            backwardComplexity = 100;
        }

        return (int) (xDistCoef*xDist +
                yDistCoef*yDist +
                BothNorthCoef * G_North * R_North +
                BothWestCoef * G_West * R_West +
                BothSouthCoef * G_South * R_South +
                BothEastCoef * G_East * R_East +
                ForwardComplexityCoef * forwardComplexity +
                BackwardComplexityCoef * backwardComplexity * currentComplexity);
    }

}

