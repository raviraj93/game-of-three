package com.takeaway.assignment.gameofthree.controllers;

import com.takeaway.assignment.gameofthree.controllers.request.GameRequest;
import com.takeaway.assignment.gameofthree.controllers.response.StartGameResponse;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import com.takeaway.assignment.gameofthree.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@Slf4j
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start-game/manual")
    public ResponseEntity<StartGameResponse> startManualGame(@RequestBody GameRequest startGameRequest) {
        if (isNumberValid(startGameRequest.getNumber())) {
            try {
                gameService.startGame(startGameRequest.getNumber());
                return ResponseEntity.ok(new StartGameResponse("Game has started"));
            } catch (PlayerNotActiveException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StartGameResponse(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StartGameResponse("Invalid number"));
        }
    }

    @GetMapping("/start-game/automatic/{justCheckingAvailability}")
    public ResponseEntity<String> startAutomaticGame(@PathVariable boolean justCheckingAvailability) {
        if (justCheckingAvailability) {
            return ResponseEntity.ok("Hey! I am available");
        }
        return startGame(getRandomNumber());
    }

    private ResponseEntity<String> startGame(Integer initialNumber) {
        try {
            gameService.startGame(initialNumber);
            return ResponseEntity.ok("Game has started");
        } catch (PlayerNotActiveException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/play")
    public ResponseEntity<String> play(@RequestBody GameMove gameMove) {
        log.info("Received number {} from the other player", gameMove.getNumber());
        try {
            gameService.play(gameMove);
            return ResponseEntity.ok("Move processed");
        } catch (PlayerNotActiveException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game Over!");
        }
    }

    private boolean isNumberValid(Integer number) {
        return number != null;
    }

    private Integer getRandomNumber() {
        // Add logic to generate a random number
        return 42; // Placeholder, implement as needed
    }
}

