package com.takeaway.assignment.gameofthree.utils;

import com.takeaway.assignment.gameofthree.config.GameConfig;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@AllArgsConstructor
public class GameUtils {

    private GameConfig config;

    public Integer getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(config.getMinimumValue(), config.getMaximumValue() + 1);
    }

    public boolean isNumberValid(Integer number) {
        return number > 1 ;
    }

    public boolean isSecondPlayerAvailable() {
        try {
            config.getRestTemplate().getForObject(config.getOtherPlayerStartGameServiceUrl() +
                    "/automatic/true",
                    String.class);
            return true;
        } catch (Exception ex) {
            log.error("Other player is unavailable or not reachable", ex);
            return false;
        }
    }

    public void sendGameMoveToOtherPlayer(GameMove gameMove) {
        config.getRestTemplate().postForObject(config.getOtherPlayerGameServiceUrl(), gameMove, String.class);
    }

    public String getSuitableUserMessage(Integer oldNumber, Integer valueAdded) {
        Integer newNumber = getNextNumber(oldNumber, valueAdded);
        String returnMsg = "";
        switch (valueAdded) {
            case 0:
                returnMsg += "just ";
                break;
            case 1:
                returnMsg += "added 1 => "+ (oldNumber+valueAdded) + " then ";
                break;
            case -1:
                returnMsg += "subtracted 1 => "+ (oldNumber+valueAdded) + " then ";
                break;
        }
        returnMsg += "divided by 3 to have the number " + newNumber;
        return returnMsg;
    }

    public Integer getNextNumber(Integer number, Integer addedNumber) {
        return (number + addedNumber) / 3;
    }
}
