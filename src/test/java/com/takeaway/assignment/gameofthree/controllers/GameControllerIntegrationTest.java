package com.takeaway.assignment.gameofthree.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.assignment.gameofthree.controllers.request.GameRequest;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.service.GameService;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.takeaway.assignment.gameofthree.constants.GameConstants.GAME_STARTED;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.INVALID_NUMBER;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.PLAYER_AVAILABLE;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
@AutoConfigureMockMvc
public class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @MockBean
    private GameUtils gameUtils;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }
    private static final String BASE_URL = "/api/v1/game/";

    @Test
    void startManualGameWithValidNumberReturnsGameStartedResponse() throws Exception {
        GameRequest gameRequest = new GameRequest(10);
        when(gameUtils.isNumberValid(gameRequest.getStartNumber())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL  + "start-game/manual")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsBytes(gameRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(GAME_STARTED));

        verify(gameService, times(1)).startGame(gameRequest.getStartNumber());
    }


    @Test
    void testStartManualGameWithInvalidNumberReturnsBadRequestResponse() throws Exception {
        GameRequest gameRequest = new GameRequest(0);
        when(gameUtils.isNumberValid(gameRequest.getStartNumber())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL  + "start-game/manual")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(gameRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(INVALID_NUMBER));

        verify(gameService, never()).startGame(any());
    }

    @Test
    void testStartAutomaticGameWithPlayerAvailableReturnsPlayerAvailableResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL  + "/start-game/automatic/true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(PLAYER_AVAILABLE));

        verify(gameService, never()).startGame(any());
    }

    @Test
    void testStartAutomaticGameWithPlayerNotAvailableReturnsGameStartedResponse() throws Exception {
        when(gameUtils.getRandomNumber()).thenReturn(5);
        when(gameUtils.isSecondPlayerAvailable()).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL  + "/start-game/automatic/false"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(GAME_STARTED));

        verify(gameService, times(1)).startGame(5); // Verify with the random number.
    }


    @Test
    void testPlayValidMoveReturnsMoveProcessedResponse() throws Exception {
        GameMove gameMove = new GameMove(7);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL  + "play")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsBytes(gameMove)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Move processed"));

        verify(gameService, times(1)).play(any(GameMove.class));
    }
}
