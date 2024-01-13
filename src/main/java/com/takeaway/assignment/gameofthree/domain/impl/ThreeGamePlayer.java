package com.takeaway.assignment.gameofthree.domain.impl;

import com.takeaway.assignment.gameofthree.domain.Player;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ThreeGamePlayer implements Player {
    private String playerName;
    @Override
    public String getPlayerName() {
        return playerName;
    }
}
