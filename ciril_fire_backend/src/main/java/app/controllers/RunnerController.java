package app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/runner")
@CrossOrigin(origins = "http://localhost:5173")
public class RunnerController {

    private int[][] grid;

    private int rows;
    private int cols;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("values.json");
        if (is != null) {
            Values values = mapper.readValue(is, Values.class);
            rows = values.getRows();
            cols = values.getCols();
            grid = new int[rows][cols];
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
        return grid;
    }

    private static class Values {
        private int rows;
        private int cols;

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public int getCols() {
            return cols;
        }

        public void setCols(int cols) {
            this.cols = cols;
        }
    }
}
