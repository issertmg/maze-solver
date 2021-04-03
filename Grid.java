import java.util.*;

public class Grid {

    private int size;  // grid size
    private Cell[][] grid; // array of cells

    public Grid (int size) {
        this.size = size;
        grid = new Cell[size][size];
        initGrid();
    }

    private void initGrid () {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                grid[i][j] = new Cell(i, j);

        grid[0][0].setType(Cell.START);
        grid[size-1][size-1].setType(Cell.END);
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getTile(int x, int y) {
        return grid[x][y];
    }

    public ArrayList<Cell> getAdjacentCells(Cell c){
        ArrayList<Cell> adjacents = new ArrayList<Cell>();
        int row = c.getRow();
        int col = c.getCol();

        //DOWN
        if (row != size-1 && grid[row+1][col].getType() != Cell.WALL)
            adjacents.add(grid[row+1][col]);
        //RIGHT
        if (col != size-1 && grid[row][col+1].getType() != Cell.WALL)
            adjacents.add(grid[row][col+1]);
        //LEFT
        if (col != 0 && grid[row][col-1].getType() != Cell.WALL)
            adjacents.add(grid[row][col-1]);
        //UP
        if (row != 0 && grid[row-1][col].getType() != Cell.WALL)
            adjacents.add(grid[row-1][col]);

// diagonal movement
//        //UP-LEFT
//        if (row != 0 && col != 0 && grid[row-1][col-1].getType() != Cell.WALL)
//            adjacents.add(grid[row-1][col-1]);
//        //UP-RIGHT
//        if (row != 0 && col != size-1 && grid[row-1][col+1].getType() != Cell.WALL)
//            adjacents.add(grid[row-1][col+1]);
//        //DOWN-LEFT
//        if (row != size-1 && col != 0 && grid[row+1][col-1].getType() != Cell.WALL)
//            adjacents.add(grid[row+1][col-1]);
//        //DOWN-RIGHT
//        if (row != size-1 && col != size-1 && grid[row+1][col+1].getType() != Cell.WALL)
//            adjacents.add(grid[row+1][col+1]);

        return adjacents;
    }
}
