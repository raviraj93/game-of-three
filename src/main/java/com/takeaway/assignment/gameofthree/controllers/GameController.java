package com.takeaway.assignment.gameofthree.controllers;

import com.takeaway.assignment.gameofthree.controllers.request.GameRequest;
import com.takeaway.assignment.gameofthree.controllers.response.StartGameResponse;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import com.takeaway.assignment.gameofthree.service.GameService;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.takeaway.assignment.gameofthree.constants.GameConstants.GAME_OVER;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.GAME_STARTED;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.INVALID_NUMBER;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.PLAYER_AVAILABLE;

@Slf4j
@RestController
@RequestMapping("/api/v1/game")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;
    private final GameUtils gameUtils;

    @PostMapping(value = "/start-game/manual", consumes={"text/plain", "application/*"})
    public ResponseEntity<StartGameResponse> startManualGame(@RequestBody GameRequest startGameRequest) {
        if (gameUtils.isNumberValid(startGameRequest.getStartNumber())) {
            try {
                gameService.startGame(startGameRequest.getStartNumber());
                return ResponseEntity.ok(new StartGameResponse(GAME_STARTED));
            } catch (PlayerNotActiveException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StartGameResponse(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StartGameResponse(INVALID_NUMBER));
        }
    }


    @GetMapping(value = "/start-game/automatic/{canStartAutomaticGame}",  consumes={"text/plain", "application/*"})
    public ResponseEntity<StartGameResponse> startAutomaticGame(@PathVariable boolean canStartAutomaticGame) {
        return canStartAutomaticGame
                ? ResponseEntity.ok(new StartGameResponse(PLAYER_AVAILABLE))
                : startGameResponse(gameUtils.getRandomNumber());
    }

    private ResponseEntity<StartGameResponse> startGameResponse(Integer initialNumber) {
        try {
            gameService.startGame(initialNumber);
            return ResponseEntity.ok(new StartGameResponse(GAME_STARTED));
        } catch (PlayerNotActiveException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new StartGameResponse(ex.getMessage()));
        }
    }

    @PostMapping(value ="/play" ,  consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> play(@RequestBody GameMove gameMove) {
        log.info("Other player played the number {}  ", gameMove.getNumber());
        try {
            gameService.play(gameMove);
            return ResponseEntity.ok("Move processed");
        } catch (PlayerNotActiveException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(GAME_OVER);
        }
    }
}

