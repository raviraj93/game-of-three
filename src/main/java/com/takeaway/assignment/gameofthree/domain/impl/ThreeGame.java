package com.takeaway.assignment.gameofthree.domain.impl;

import com.takeaway.assignment.gameofthree.domain.Game;
import com.takeaway.assignment.gameofthree.domain.GameMove;
import com.takeaway.assignment.gameofthree.domain.Player;
import com.takeaway.assignment.gameofthree.utils.GameUtils;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

@Component
@Getter
public class ThreeGame implements Game {

    private final GameUtils gameUtils;
    private final List<Player> players;

    public ThreeGame(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
        this.players = synchronizedList(new ArrayList<>());
    }

    @Override
    public synchronized void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public synchronized void removePlayer() {
        if (!players.isEmpty()) {
            players.remove(0);
        }
    }

    @Override
    public void sendGameMoveToOtherPlayer(GameMove gameMove) {
        gameUtils.sendGameMoveToOtherPlayer(gameMove);
    }

    @Override
    public String getSuitableUserMessage(int oldNumber, int valueAdded) {
        return gameUtils.getSuitableUserMessage(oldNumber, valueAdded);
    }
}
