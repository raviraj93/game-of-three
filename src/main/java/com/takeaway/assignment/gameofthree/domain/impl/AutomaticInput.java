package com.takeaway.assignment.gameofthree.domain.impl;

import com.takeaway.assignment.gameofthree.domain.Input;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class AutomaticInput implements Input {

    @Value("game.max_value")
    private final Integer maximumValue;

    @Value("game.min_value")
    private final Integer minimumValue;

    @Override
    public int getInput() {
        return ThreadLocalRandom.current().nextInt(minimumValue, minimumValue + 1);
    }
}
