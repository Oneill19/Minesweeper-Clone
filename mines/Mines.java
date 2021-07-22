package mines;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Mines {
    //subclass to save a point on the board
    private class Point {
        private int x;
        private int y;
        private boolean isBomb = false;
        private boolean isPressed = false;
        private boolean isFlag = false;
        private Set<Point> neighbours;

        //constructor
        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        //set the neighbours of the point
        private void setNeighbours(Set<Point> neighbours) {
            this.neighbours = neighbours;
        }

        //sum the number of the bomb neighbours
        private int bombNeighbours() {
            int count = 0;
            for (Point p : neighbours)
                if (p.isBomb)
                    count++;
            return count;
        }

        //create a set of the neighbours of the point
        private Set<Point> createNeighbours(int i, int j) {
            Set<Point> neighbours = new HashSet<>();
            if (checkRange(i + 1, j))
                neighbours.add(board[i + 1][j]);
            if (checkRange(i - 1, j))
                neighbours.add(board[i - 1][j]);
            if (checkRange(i, j + 1))
                neighbours.add(board[i][j + 1]);
            if (checkRange(i, j - 1))
                neighbours.add(board[i][j - 1]);
            if (checkRange(i + 1, j + 1))
                neighbours.add(board[i + 1][j + 1]);
            if (checkRange(i - 1, j - 1))
                neighbours.add(board[i - 1][j - 1]);
            if (checkRange(i + 1, j - 1))
                neighbours.add(board[i + 1][j - 1]);
            if (checkRange(i - 1, j + 1))
                neighbours.add(board[i - 1][j + 1]);
            return neighbours;
        }
    }

    private int height;
    private int width;
    private boolean showAll = false;
    private Point[][] board;

    //constructor
    public Mines(int height, int width, int numMines) {
        board = new Point[height][width];
        this.height = height;
        this.width = width;
        initializeBoard(numMines);  //initialize all the points on the board
    }

    //add mine to the board
    public boolean addMine(int i, int j) {
        if (!checkRange(i, j) || board[i][j].isBomb)    //if there is already a bomb or the the indexes are not in range
            return false;
        board[i][j].isBomb = true;
        return true;
    }

    //open a point on the board
    public boolean open(int i, int j) {
        if (board[i][j].isBomb) //if the point is bomb return false
            return false;
        if (board[i][j].isPressed)  //if the point already opened return true
            return true;
        board[i][j].isPressed = true;   //open the point
        if (board[i][j].bombNeighbours() == 0)  //if all the neighbours aren't bombs, open them and do it recursively
            for (Point p : board[i][j].neighbours)  //iterate over the neighbours
                open(p.x, p.y);
        return true;    //return true
    }

    //toggle flag on the board
    public void toggleFlag(int x, int y) {
        board[x][y].isFlag = !board[x][y].isFlag;
    }

    //check if all the points except the bombs are open
    public boolean isDone() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (!board[i][j].isBomb && !board[i][j].isPressed)
                    return false;
        return true;
    }

    //return the string value of each point
    public String get(int i, int j) {
        if (!board[i][j].isPressed && !showAll)
            return board[i][j].isFlag ? "F" : ".";
        return board[i][j].isBomb ? "X" : board[i][j].bombNeighbours() == 0 ? " " : String.valueOf(board[i][j].bombNeighbours());
    }

    //set showAll to show the value of all the points on the board
    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    //return the object in string representation
    public String toString() {
        StringBuilder returnValue = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                returnValue.append(get(i, j));
            returnValue.append("\n");
        }
        return returnValue.toString();
    }

    //initialize the board
    private void initializeBoard(int numMines) {
        //create all the points
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                board[i][j] = new Point(i, j);
        //set all the neighbours of the points
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                board[i][j].setNeighbours(board[i][j].createNeighbours(i, j));
        //enter numMines bombs randomly to the board
        int x, y;
        Random rand = new Random();
        while (numMines > 0) {
            x = rand.nextInt(height);
            y = rand.nextInt(width);
            if (!board[x][y].isBomb) {
                board[x][y].isBomb = true;
                numMines--;
            }
        }
    }

    //check if the indexes are in range
    private boolean checkRange(int i, int j) {
        return i < height && i >= 0 && j < width && j >= 0;
    }
}
