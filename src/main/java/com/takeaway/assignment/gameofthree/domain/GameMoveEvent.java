package com.takeaway.assignment.gameofthree.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GameMoveEvent extends ApplicationEvent {
    private final GameMove gameMove;

    public GameMoveEvent(Object source, GameMove gameMove) {
        super(source);
        this.gameMove = gameMove;
    }
}

