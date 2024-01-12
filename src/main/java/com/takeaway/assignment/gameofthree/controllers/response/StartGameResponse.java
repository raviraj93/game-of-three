package com.takeaway.assignment.gameofthree.controllers.response;

import lombok.Data;

@Data
public class StartGameResponse {
    private String message;

    public StartGameResponse(String message) {
        this.message = message;
    }
}

