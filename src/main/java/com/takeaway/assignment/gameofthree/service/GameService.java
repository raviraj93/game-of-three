package com.takeaway.assignment.gameofthree.service;

import com.takeaway.assignment.gameofthree.constants.GameConstants;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.takeaway.assignment.gameofthree.constants.GameConstants.*;

@Service
@Slf4j
public class GameService {
    private final GameUtils gameUtils;

    public GameService(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
    }

    public void startGame(Integer initialNumber) {
        if (gameUtils.isSecondPlayerAvailable()) {
            log.info("the game is starting with initial number {} and sending it to the other player" , initialNumber);
            GameMove gameMove = new GameMove(initialNumber);
            gameUtils.sendGameMoveToOtherPlayer(gameMove);
        } else {
            throw new PlayerNotActiveException(OTHER_PLAYER_NOT_AVAILABLE);
        }
    }

    public void play(GameMove otherPlayerMove) {
        int opponentNumber = otherPlayerMove.getNumber();

        if (opponentNumber == 1) {
            handleOpponentWin();
            return;
        }

        int addedNumber = calculateAddedNumber(opponentNumber);
        int newNumber = gameUtils.getNextNumber(opponentNumber, addedNumber);

        logOpponentMove(opponentNumber, addedNumber);

        if (newNumber == 1) {
            log.info(WINNER);
        }

        GameMove myMove = new GameMove(newNumber);

        if (gameUtils.isSecondPlayerAvailable()) {
            sendMyMoveToOtherPlayer(myMove);
        } else {
            handleSecondPlayerNotAvailable();
        }
    }

    private void handleOpponentWin() {
        log.info(OTHER_PLAYER_WIN);
    }

    private void logOpponentMove(int opponentNumber, int addedNumber) {
        log.info("Processing opponent move. Opponent's number: {}, Added number: {}. {}",
                opponentNumber, addedNumber, gameUtils.getSuitableUserMessage(opponentNumber, addedNumber));
    }

    private void sendMyMoveToOtherPlayer(GameMove myMove) {
        log.info("Sending {} to the other player", myMove.getNumber());
        gameUtils.sendGameMoveToOtherPlayer(myMove);
    }


    private int calculateAddedNumber(int number) {
        return (number % 3 == 0) ? 0 : ((number + 1) % 3 == 0) ? 1 : -1;
    }

    private void handleSecondPlayerNotAvailable() {
        log.info(OTHER_PLAYER_NOT_AVAILABLE_ANYMORE);
        throw new PlayerNotActiveException(OTHER_PLAYER_NOT_AVAILABLE_ANYMORE);
    }
}

