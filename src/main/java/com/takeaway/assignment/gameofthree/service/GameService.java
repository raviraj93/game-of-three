package com.takeaway.assignment.gameofthree.service;

import com.takeaway.assignment.gameofthree.domain.Game;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.exception.PlayerNotActiveException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameService {
    private final Game game;

    public void startGame(Integer initialNumber) throws PlayerNotActiveException {
        game.startGame(initialNumber);
    }

    public void play(GameMove gameMove) throws PlayerNotActiveException {
        game.play(gameMove);
    }
}

