package app.services;

import app.models.GridConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

/*
 *  The service of the simulation
 *  There are 3 states in the grid :
 *  0 = Tree
 *  1 = Fire
 *  2 = Ashes
 */
@Service
public class RunnerService {

    private int[][] grid;
    private GridConfig gridConfig;
    private int propagationPercentage;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("values.json");

        if (is != null) {

            // Mapping of the json values in the Grid Model
            gridConfig = mapper.readValue(is, GridConfig.class);

            grid = new int[gridConfig.getRows()][gridConfig.getCols()];
            propagationPercentage = gridConfig.getPropagationPercentage();

            // Initialisation of the grid with the initials Fires
            for (List<Integer> coord : gridConfig.getInitialOnes()) {
                int row = coord.get(0);
                int col = coord.get(1);
                if (isValidCoordinate(row, col)) {
                    grid[row][col] = 1;
                }
            }
        } else {
            throw new IllegalStateException("values.json not found in resources");
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] incrementGrid() {
        Random random = new Random();
        // Create a results Grid
        int[][] newGrid = copyGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    incrementAdjacentCells(newGrid, i, j, random);
                }
            }
        }

        grid = newGrid;
        return grid;
    }

    public int[][] resetGrid() {
        grid = new int[gridConfig.getRows()][gridConfig.getCols()];
        for (List<Integer> coord : gridConfig.getInitialOnes()) {
            int row = coord.get(0);
            int col = coord.get(1);
            if (isValidCoordinate(row, col)) {
                grid[row][col] = 1;
            }
        }
        return grid;
    }

    private void incrementAdjacentCells(int[][] newGrid, int row, int col, Random random) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1} // up, down, left, right
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            // if the values are outside of the grid, they will be ignored
            if (isValidCoordinate(newRow, newCol) && newGrid[newRow][newCol] < 2) {
                /*
                 * A random number between 0 and 100 is generated for each adjacent cell
                 * The generated number is compared with the specified propagation percentage
                 */
                if (random.nextInt(100) < propagationPercentage) {
                    newGrid[newRow][newCol]++;
                }
            }
        }

        // 2 is the maximum state
        if (newGrid[row][col] < 2) {
            newGrid[row][col]++;
        }
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < gridConfig.getRows() && col >= 0 && col < gridConfig.getCols();
    }

    private int[][] copyGrid() {
        int[][] newGrid = new int[gridConfig.getRows()][gridConfig.getCols()];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, grid[i].length);
        }
        return newGrid;
    }
}
