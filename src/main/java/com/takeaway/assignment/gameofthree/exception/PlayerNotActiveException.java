package com.takeaway.assignment.gameofthree.exception;

public class PlayerNotActiveException extends RuntimeException{
    public PlayerNotActiveException(String message) {
        super(message);
    }
}

