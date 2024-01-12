package com.takeaway.assignment.gameofthree.service;

import com.takeaway.assignment.gameofthree.constants.GameConstants;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {
    private final GameUtils gameUtils;

    private Integer number;

    private Integer addedNumber;

    private Integer newNumber;

    public GameService(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
    }

    public void startGame(Integer initialNumber) {
        if (gameUtils.isSecondPlayerAvailable()) {
            log.info("the game is starting with initial number {} and sending it to the other player" , initialNumber);
            GameMove gameMove = new GameMove(initialNumber);
            gameUtils.sendGameMoveToOtherPlayer(gameMove);
        } else {
            throw new PlayerNotActiveException(GameConstants.OTHER_PLAYER_NOT_AVAILABLE);
        }
    }

    public void play(GameMove otherPlayerMove) {
        number = otherPlayerMove.getNumber();
        if (number == 1) {
            log.info(GameConstants.OTHER_PLAYER_WIN);
            return;
        }
        GameMove myMove;
        if (number % 3 == 0) {
            addedNumber = 0;
        } else {
            addedNumber = (number + 1) % 3 == 0 ? 1 : -1;
        }
        newNumber = gameUtils.getNewNumber(number, addedNumber);
        log.info(gameUtils.getSuitableUserMessage(number, addedNumber));
        if (newNumber == 1) {
            log.info(GameConstants.WINNER);
        }
        myMove = new GameMove(newNumber);
        if (gameUtils.isSecondPlayerAvailable()) {
            log.info("sending {} to the other player", myMove.getNumber());
            gameUtils.sendGameMoveToOtherPlayer(myMove);
        } else {
            log.info(GameConstants.OTHER_PLAYER_NOT_AVAILABLE_ANYMORE);
            throw new PlayerNotActiveException(GameConstants.OTHER_PLAYER_NOT_AVAILABLE_ANYMORE);
        }
    }
}

