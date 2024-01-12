package com.takeaway.assignment.gameofthree.controllers;

import com.takeaway.assignment.gameofthree.controllers.request.GameRequest;
import com.takeaway.assignment.gameofthree.controllers.response.StartGameResponse;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import com.takeaway.assignment.gameofthree.service.GameService;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.takeaway.assignment.gameofthree.constants.GameConstants.GAME_OVER;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.GAME_STARTED;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.INVALID_NUMBER;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.PLAYER_AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private GameUtils gameUtils;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testStartManualGameWithValidNumberHaveGameStartedResponse() {
        GameRequest gameRequest = new GameRequest(10);
        when(gameUtils.isNumberValid(gameRequest.getStartNumber())).thenReturn(true);

        ResponseEntity<StartGameResponse> response = gameController.startManualGame(gameRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(GAME_STARTED, response.getBody().getMessage());
        verify(gameService, times(1)).startGame(gameRequest.getStartNumber());
    }

    @Test
    void testStartManualGameWithInvalidNumberReturnsBadRequestResponse() {
        GameRequest gameRequest = new GameRequest(0);
        when(gameUtils.isNumberValid(gameRequest.getStartNumber())).thenReturn(false);

        ResponseEntity<StartGameResponse> response = gameController.startManualGame(gameRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(INVALID_NUMBER, response.getBody().getMessage());
        verify(gameService, never()).startGame(any());
    }

    @Test
    void testStartManualGameWithPlayerNotActiveReturnsNotFoundResponse() {
        GameRequest gameRequest = new GameRequest(5);
        when(gameUtils.isNumberValid(gameRequest.getStartNumber())).thenReturn(true);
        doThrow(new PlayerNotActiveException("Player not active"))
                .when(gameService).startGame(gameRequest.getStartNumber());

        ResponseEntity<StartGameResponse> response = gameController.startManualGame(gameRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Player not active", response.getBody().getMessage());
        verify(gameService, times(1)).startGame(gameRequest.getStartNumber());
    }

    @Test
    void testStartAutomaticGameWithCanStartAutomaticGameReturnsPlayerAvailableResponse() {
        when(gameUtils.getRandomNumber()).thenReturn(15);

        ResponseEntity<StartGameResponse> response = gameController.startAutomaticGame(true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PLAYER_AVAILABLE, response.getBody().getMessage());
        verify(gameService, never()).startGame(any());
    }

    @Test
    void testStartAutomaticGameWithCannotStartAutomaticGameReturnsGameStartedResponse() {
        when(gameUtils.getRandomNumber()).thenReturn(20);

        ResponseEntity<StartGameResponse> response = gameController.startAutomaticGame(false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(GAME_STARTED, response.getBody().getMessage());
        verify(gameService, times(1)).startGame(20);
    }

    @Test
    void testPlayWithValidGameMoveReturnsMoveProcessedResponse() {
        GameMove gameMove = new GameMove(8);

        ResponseEntity<String> response = gameController.play(gameMove);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Move processed", response.getBody());
        verify(gameService, times(1)).play(gameMove);
    }

    @Test
    void testPlayWithPlayerNotActiveReturnsGameOverResponse() {
        GameMove gameMove = new GameMove(12);
        doThrow(new PlayerNotActiveException("Player not active")).when(gameService).play(gameMove);

        ResponseEntity<String> response = gameController.play(gameMove);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(GAME_OVER, response.getBody());
        verify(gameService, times(1)).play(gameMove);
    }
}

