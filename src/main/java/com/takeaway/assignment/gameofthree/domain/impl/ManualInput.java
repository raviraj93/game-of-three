package com.takeaway.assignment.gameofthree.domain.impl;

import com.takeaway.assignment.gameofthree.domain.Input;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ManualInput implements Input {
    @Override
    public int getInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your move (-1, 0, or 1): ");
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            // Recursive call to get a valid input
            return getInput();
        }
    }
}
