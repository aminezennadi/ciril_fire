package app.services;

import app.models.Grid;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RunnerService {

    private int[][] grid;
    private Grid gridConfig;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("values.json");

        if (is != null) {
            gridConfig = mapper.readValue(is, Grid.class);

            grid = new int[gridConfig.getRows()][gridConfig.getCols()];

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
        int[][] newGrid = copyGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    incrementAdjacentCells(newGrid, i, j);
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

    private void incrementAdjacentCells(int[][] newGrid, int row, int col) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (isValidCoordinate(newRow, newCol) && newGrid[newRow][newCol] < 2) {
                newGrid[newRow][newCol]++;
            }
        }

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
