package com.takeaway.assignment.gameofthree.listener;

import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.domain.GameMoveEvent;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class GameMoveEventListenerTest {

    @Mock
    private GameUtils gameUtils;

    @InjectMocks
    private GameMoveEventListener eventListener;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testOnApplicationEventWithAvailablePlayerShouldSendGameMoveToOtherPlayer() {
        GameMove gameMove = new GameMove(5);
        GameMoveEvent event = new GameMoveEvent(this, gameMove);

        when(gameUtils.isSecondPlayerAvailable()).thenReturn(true);

        eventListener.onApplicationEvent(event);

        verify(gameUtils, times(1)).sendGameMoveToOtherPlayer(gameMove);
    }

    @Test
    void testOnApplicationEventWithUnavailablePlayerShouldNotSendGameMoveToOtherPlayer() {
        GameMove gameMove = new GameMove(8);
        GameMoveEvent event = new GameMoveEvent(this, gameMove);

        when(gameUtils.isSecondPlayerAvailable()).thenReturn(false);

        eventListener.onApplicationEvent(event);

        verify(gameUtils, never()).sendGameMoveToOtherPlayer(gameMove);
    }
}
