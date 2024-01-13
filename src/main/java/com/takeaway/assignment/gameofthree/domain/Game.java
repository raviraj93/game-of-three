package com.takeaway.assignment.gameofthree.domain;

import java.util.List;

public interface Game {
    void sendGameMoveToOtherPlayer(GameMove gameMove);
    String getSuitableUserMessage(int oldNumber, int valueAdded);
    void addPlayer(Player player);
    void removePlayer();
}
