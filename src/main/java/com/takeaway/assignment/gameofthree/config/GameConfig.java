package com.takeaway.assignment.gameofthree.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Data
public class GameConfig {
    private final Integer minimumValue;
    private final Integer maximumValue;
    private final String otherPlayerGameServiceUrl;
    private final String otherPlayerStartGameServiceUrl;
    private final RestTemplate restTemplate;

    public GameConfig(@Value("${game.random-number.min}") Integer minimumValue,
                      @Value("${game.random-number.max}") Integer maximumValue,
                      @Value("${game.other-player.play-url}") String otherPlayerGameServiceUrl,
                      @Value("${game.other-player.start-url}") String otherPlayerStartGameServiceUrl,
                      RestTemplateBuilder restTemplateBuilder) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.otherPlayerGameServiceUrl = otherPlayerGameServiceUrl;
        this.otherPlayerStartGameServiceUrl = otherPlayerStartGameServiceUrl;
        this.restTemplate = restTemplateBuilder.build();
    }
}
