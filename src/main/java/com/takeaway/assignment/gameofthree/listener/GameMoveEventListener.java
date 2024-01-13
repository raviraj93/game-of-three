package com.takeaway.assignment.gameofthree.listener;

import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.domain.GameMoveEvent;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class GameMoveEventListener implements ApplicationListener<GameMoveEvent> {

    private final GameUtils gameUtils;
    public void onApplicationEvent(GameMoveEvent event) {
        GameMove gameMove = event.getGameMove();
        log.info("Received game move: {}", gameMove.getNumber());

        if (gameUtils.isSecondPlayerAvailable()) {
            log.info("Sending game move to the other player...");
            gameUtils.sendGameMoveToOtherPlayer(gameMove);
            log.info("Game move sent successfully.");
        } else {
            log.warn("Other player is not available or not reachable. Cannot send game move.");
        }
    }
}

