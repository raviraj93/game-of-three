package com.takeaway.assignment.gameofthree.utils;

import com.takeaway.assignment.gameofthree.config.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


class GameUtilsTest {

    @Mock
    private GameConfig config;

    @InjectMocks
    private GameUtils gameUtils;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        initMocks(this);
        when(config.getRestTemplate()).thenReturn(restTemplate);

    }

    @Test
    void testGetRandomNumberShouldReturnNumberWithinRange() {
        when(config.getMinimumValue()).thenReturn(1);
        when(config.getMaximumValue()).thenReturn(10);

        int randomNumber = gameUtils.getRandomNumber();

        assertTrue(randomNumber >= 1);
        assertTrue(randomNumber <= 10);
    }

    @Test
    void testIsNumberValidShouldReturnTrue() {
        assertTrue(gameUtils.isNumberValid(5));
    }

    @Test
    void testIsNumberValidShouldReturnFalseForInvalidNumber() {
        assertFalse(gameUtils.isNumberValid(0));
    }

    @Test
    void testIsSecondPlayerAvailableShouldReturnTrueForServiceIsReachable() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("34");

        assertTrue(gameUtils.isSecondPlayerAvailable());

        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }

    @Test
    void testIsSecondPlayerAvailableShouldReturnFalseForServiceIsNotReachable() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("Service not reachable"));

        assertFalse(gameUtils.isSecondPlayerAvailable());

        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }

    @Test
    void testGetSuitableUserMessageShouldReturnCorrectMessageForAdding() {
        String message = gameUtils.getSuitableUserMessage(5, 1);
        assertEquals("added 1 => 6 then divided by 3 to have the number 2", message);
    }

    @Test
    void testGetSuitableUserMessageShouldReturnCorrectMessageForSubtracting() {
        String message = gameUtils.getSuitableUserMessage(5, -1);
        assertEquals("subtracted 1 => 4 then divided by 3 to have the number 1", message);
    }

    @Test
    void testGetNextNumberShouldReturnCorrectNextNumber() {
        int nextNumber = gameUtils.getNextNumber(5, 1);
        assertEquals(2, nextNumber);
    }
}
