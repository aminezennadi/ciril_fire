package app.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/counter")
@CrossOrigin(origins = "http://localhost:5173") // Adresse de votre front-end
public class RunnerController {

    private int counter = 0;

    @GetMapping
    public int getCounter() {
        return counter;
    }

    @PostMapping("/increment")
    public int incrementCounter() {
        counter++;
        return counter;
    }
}
