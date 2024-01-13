package com.takeaway.assignment.gameofthree.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.domain.GameMoveEvent;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class GameServiceTest {

    @Mock
    private GameUtils gameUtils;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testStartGameWhenOtherPlayerAvailable() {
        when(gameUtils.isSecondPlayerAvailable()).thenReturn(true);

        gameService.startGame(42);

        verify(eventPublisher, times(1)).publishEvent(any(GameMoveEvent.class));
    }

    @Test
    void testStartGameThrowExceptionWhenOtherPlayerNotAvailable() {
        when(gameUtils.isSecondPlayerAvailable()).thenReturn(false);

        assertThrows(PlayerNotActiveException.class, () -> gameService.startGame(42));
    }

    @Test
    void testPlayWithOppositionPlayerMove() {
        GameMove opponentMove = new GameMove(54);

        Logger logger = (Logger) LoggerFactory.getLogger(GameService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        when(gameUtils.getNextNumber(anyInt(), anyInt())).thenReturn(1);
        when(gameUtils.isSecondPlayerAvailable()).thenReturn(true);
        when(gameUtils.getSuitableUserMessage(anyInt(), anyInt())).thenReturn("5");

        gameService.play(opponentMove);

        assertEquals(3, listAppender.list.size());
        ILoggingEvent loggingEvent = listAppender.list.get(0);
        assertTrue(loggingEvent.getFormattedMessage().contains("Processing opponent move." +
                " Opponent's number: 54, Added number: 0. 5"));

        logger.detachAppender(listAppender);
    }

    @Test
    void testPlayWithMoveSentToOtherPlayer() {
        GameMove opponentMove = new GameMove(5);

        when(gameUtils.getNextNumber(anyInt(), anyInt())).thenReturn(3);
        when(gameUtils.isSecondPlayerAvailable()).thenReturn(true);

        gameService.play(opponentMove);

        verify(eventPublisher, times(1)).publishEvent(any(GameMoveEvent.class));
    }

    @Test
    void testPlayWhenOpponentMoveWinsLogMessage() {
        GameMove opponentMove = new GameMove(4);

        Logger logger = (Logger) LoggerFactory.getLogger(GameService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        when(gameUtils.getNextNumber(anyInt(), anyInt())).thenReturn(1);
        when(gameUtils.isSecondPlayerAvailable()).thenReturn(true);

        gameService.play(opponentMove);

        assertTrue(listAppender.list
                .stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Congratulations! you win, Yayyyyyy.  " +
                        "Now give me a Party")));

        logger.detachAppender(listAppender);
    }

    @Test
    void testPlayOpponentMoveAndPlayerNotAvailableExceptionThrown() {
        GameMove opponentMove = new GameMove(7);

        when(gameUtils.isSecondPlayerAvailable()).thenReturn(false);

        assertThrows(PlayerNotActiveException.class, () -> gameService.play(opponentMove));
    }
}
