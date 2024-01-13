package com.takeaway.assignment.gameofthree.service;

import com.takeaway.assignment.gameofthree.domain.Game;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.domain.GameMoveEvent;
import com.takeaway.assignment.gameofthree.domain.impl.ThreeGamePlayer;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.takeaway.assignment.gameofthree.constants.GameConstants.OTHER_PLAYER_NOT_AVAILABLE;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.OTHER_PLAYER_NOT_AVAILABLE_ANYMORE;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.OTHER_PLAYER_WIN;
import static com.takeaway.assignment.gameofthree.constants.GameConstants.WINNER;

@Service
@Slf4j
public class GameService {
    private final GameUtils gameUtils;
    private final Game game;
    private final ApplicationEventPublisher eventPublisher;
    private final String playerName;

    public GameService(GameUtils gameUtils, ApplicationEventPublisher eventPublisher, Game game,
                       @Value("${spring.application.name}") String playerName) {
        this.gameUtils = gameUtils;
        this.eventPublisher = eventPublisher;
        this.game = game;
        this.playerName = playerName;
    }
    public void startGame(Integer initialNumber) {
        game.addPlayer(new ThreeGamePlayer(playerName));
        if (gameUtils.isSecondPlayerAvailable()) {
            log.info("the game is starting with initial number {} and sending it to the other player" , initialNumber);
            game.addPlayer(new ThreeGamePlayer(playerName + "_ 2"));
            GameMove gameMove = new GameMove(initialNumber);
            eventPublisher.publishEvent(new GameMoveEvent(this , gameMove));
        } else {
            throw new PlayerNotActiveException(OTHER_PLAYER_NOT_AVAILABLE);
        }
    }

    public void play(GameMove otherPlayerMove) {
        int opponentNumber = otherPlayerMove.getNumber();

        if (opponentNumber == 1) {
            removePlayer();
            log.info(OTHER_PLAYER_WIN);
            return;
        }

        int addedNumber = calculateAddedNumber(opponentNumber);
        int newNumber = gameUtils.getNextNumber(opponentNumber, addedNumber);

        logOpponentMove(opponentNumber, addedNumber);

        if (newNumber == 1) {
            removePlayer();
            log.info(WINNER);
        }

        GameMove myMove = new GameMove(newNumber);

        if (gameUtils.isSecondPlayerAvailable()) {
            sendMyMoveToOtherPlayer(myMove);
        } else {
            handleSecondPlayerNotAvailable();
        }
    }


    public void logOpponentMove(int opponentNumber, int addedNumber) {
        log.info("Processing opponent move. Opponent's number: {}, Added number: {}. {}",
                opponentNumber, addedNumber, game.getSuitableUserMessage(opponentNumber, addedNumber));
    }

    public void sendMyMoveToOtherPlayer(GameMove move) {
        log.info("Sending {} to the other player", move.getNumber());
        eventPublisher.publishEvent(new GameMoveEvent(this , move));
    }


    private int calculateAddedNumber(int number) {
        return (number % 3 == 0) ? 0 : ((number + 1) % 3 == 0) ? 1 : -1;
    }

    private void handleSecondPlayerNotAvailable() {
        log.info(OTHER_PLAYER_NOT_AVAILABLE_ANYMORE);
        throw new PlayerNotActiveException(OTHER_PLAYER_NOT_AVAILABLE_ANYMORE);
    }

    private void removePlayer() {
        game.removePlayer();
    }
}

