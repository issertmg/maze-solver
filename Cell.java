import java.util.ArrayList;

public class Cell {
    public static int SPACE = 0;
    public static int START = 1;
    public static int END = 2;
    public static int WALL = 3;
    public static int VISITED = 4;
    public static int PATH = 5;
    private int row;
    private int col;
    private int type;

    private int heuristic;
    private int pathCost;
    private int total;
    public Cell parent;

    public Cell (int row, int col) {
        this.row = row;
        this.col = col;
        this.type = SPACE;
        parent = null;
    }

    public Cell (Cell parent, int row, int col) {
        this.row = row;
        this.col = col;
        this.type = SPACE;
        this.parent = parent;
    }

    public int getRow () {
        return row;
    }

    public int getCol () {
        return col;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int computeDistance (Cell cell) {
        return (cell.getRow() - this.getRow()) * (cell.getRow() - this.getRow()) + (cell.getCol() - this.getCol()) * (cell.getCol() - this.getCol());
    }

    public int getHeuristic () {
        return heuristic;
    }

    public int getPathCost () {
        return pathCost;
    }

    public int getTotal () {
        return total;
    }

    public void setHeuristic (int heuristic) {
        this.heuristic = heuristic;
    }

    public void setPathCost (int pathCost) {
        this.pathCost = pathCost;
    }

    public void setTotal (int total) {
        this.total = total;
    }

    public void addToTotal (int num){
        this.total += num;
    }

    public void computeHeuristic(Cell goal){
        setHeuristic(computeDistance(goal));
    }

    public void computePathCost(Cell start){
        setPathCost(computeDistance(start));
    }

    public void computeTotal(Cell goal, Cell start) {
        computeHeuristic(goal);
        computePathCost(start);
        int f = heuristic + pathCost;
        setTotal(f);
    }

    @Override
    public boolean equals (Object o) {
        Cell c = (Cell) o;
        return this.row == c.getRow() && this.col == c.getCol();
    }  
}
