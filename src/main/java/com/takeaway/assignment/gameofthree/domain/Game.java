package com.takeaway.assignment.gameofthree.domain;

import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Game {
    private boolean secondPlayerActive;
    private Integer targetNumber;

    public void startGame(Integer initialNumber) throws PlayerNotActiveException {
        secondPlayerActive = true;
        targetNumber = initialNumber;
    }

    public void play(GameMove gameMove) throws PlayerNotActiveException {

        if (!secondPlayerActive) {
            throw new PlayerNotActiveException("Second player is not active");
        }

        if (targetNumber == 1) {
            secondPlayerActive = false;
        } else {

            targetNumber = (targetNumber + gameMove.getNumber()) / 3;
        }
    }
}

