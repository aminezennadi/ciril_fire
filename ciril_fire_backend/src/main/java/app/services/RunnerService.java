package app.services;

import app.models.GridConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

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

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("values.json");

        if (is != null) {

            // Mapping of the json values in the Grid Model
            gridConfig = mapper.readValue(is, GridConfig.class);

            grid = new int[gridConfig.getRows()][gridConfig.getCols()];

            // Initialisation of the grid with the initials Fires
            resetGrid();

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

        IntStream.range(0, grid.length) // Stream over row indices
                .forEach(i -> IntStream.range(0, grid[i].length)
                        .filter(j -> grid[i][j] == 1) // Process only on fire cells (1)
                        .forEach(j -> incrementAdjacentCells(newGrid, i, j, random))
                );

        grid = newGrid;

        return grid;
    }

    public int[][] resetGrid() {
        grid = new int[gridConfig.getRows()][gridConfig.getCols()];
        gridConfig.getInitialOnes().stream()
                .filter(coord -> isValidCoordinate(coord.get(0), coord.get(1))) // Filter valid coordinates
                .forEach(coord -> grid[coord.get(0)][coord.get(1)] = 1);
        return grid;
    }

    private void incrementAdjacentCells(int[][] newGrid, int row, int col, Random random) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1} // up, down, left, right
        };

        /*
         * A random number between 0 and 100 is generated for each adjacent cell
         * The generated number is compared with the specified propagation percentage
         */
        Arrays.stream(directions)
                .map(direction -> new int[] {row + direction[0], col + direction[1]})
                .filter(coords -> isValidCoordinate(coords[0], coords[1]))
                .filter(coords -> newGrid[coords[0]][coords[1]] < 2) // Filter cells with value less than 2 (not ashes)
                .filter(coords -> random.nextInt(100) < gridConfig.getPropagationPercentage())
                .forEach(coords -> newGrid[coords[0]][coords[1]]++); // Increment


        // increment the current cell
        newGrid[row][col]++;
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < gridConfig.getRows() && col >= 0 && col < gridConfig.getCols();
    }

    private int[][] copyGrid() {
        int[][] newGrid = new int[gridConfig.getRows()][gridConfig.getCols()];

        IntStream.range(0, grid.length)
                .forEach(i -> System.arraycopy(grid[i], 0, newGrid[i], 0, grid[i].length));

        return newGrid;
    }
}
