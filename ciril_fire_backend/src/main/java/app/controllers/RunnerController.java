package app.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/runner")
@CrossOrigin(origins = "http://localhost:5173") // Adresse du front-end
public class RunnerController {

    private int counter = 0;

    @GetMapping
    public int getCounter() {
        return counter;
    }

    @PostMapping("/increment")
    public int incrementCounter() {
        if (counter < 2) { // Limite supérieure : 2
            counter++;
        }
        return counter;
    }
}