package app.controllers;

import app.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/runner")
@CrossOrigin(origins = "http://localhost:5173")
public class RunnerController {

    @Autowired
    private RunnerService runnerService;

    @GetMapping("/grid")
    public int[][] getGrid() {
        return runnerService.getGrid();
    }

    @PostMapping("/increment")
    public int[][] incrementGrid() {
        return runnerService.incrementGrid();
    }

    @PostMapping("/reset")
    public int[][] resetGrid() {
        return runnerService.resetGrid();
    }
}
