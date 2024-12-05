package app.controllers;

import app.models.Grid;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/runner")
@CrossOrigin(origins = "http://localhost:5173")
public class RunnerController {

    private int[][] grid;

    private int rows;
    private int cols;
    private List<List<Integer>> initialOnes;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("values.json");
        if (is != null) {
            Grid values = mapper.readValue(is, Grid.class);
            rows = values.getRows();
            cols = values.getCols();
            initialOnes = values.getInitialOnes();
            grid = new int[rows][cols];
            for (List<Integer> coord : initialOnes) {
                int row = coord.get(0) - 1;
                int col = coord.get(1) - 1;
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    grid[row][col] = 1;
                }
            }
        } else {
            throw new IllegalStateException("values.json not found in resources");
        }
    }

    @GetMapping("/grid")
    public int[][] getGrid() {
        return grid;
    }

    @PostMapping("/increment")
    public int[][] incrementGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] < 2) {
                    grid[i][j]++;
                }
            }
        }
        return grid;
    }

    @PostMapping("/reset")
    public int[][] resetGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }
        for (List<Integer> coord : initialOnes) {
            int row = coord.get(0) - 1;
            int col = coord.get(1) - 1;
            if (row >= 0 && row < rows && col >= 0 && col < cols) {
                grid[row][col] = 1;
            }
        }
        return grid;
    }
}
