package app.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/runner")
@CrossOrigin(origins = "http://localhost:5173") // Adresse de votre front-end
public class RunnerController {

    private int[][] grid;

    public RunnerController() {
        int rows = 5;
        int cols = 5;
        grid = new int[rows][cols]; // Initialisation de la grille avec des 0
    }

    @GetMapping("/grid")
    public int[][] getGrid() {
        return grid; // Retourne la grille actuelle
    }

    @PostMapping("/increment")
    public int[][] incrementGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] < 2) {
                    grid[i][j]++; // Incrémente les cellules jusqu'à un maximum de 2
                }
            }
        }
        return grid; // Retourne la grille après incrémentation
    }

    @PostMapping("/reset")
    public int[][] resetGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0; // Remet chaque cellule à 0
            }
        }
        return grid; // Retourne la grille après réinitialisation
    }
}
