package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.Player;
import com.thg.accelerator23.connectn.ai.foursight.Solver;

public class FourSight2 extends Player {

    private Solver solver;
    private int moves;

    public FourSight2(Counter counter) {
        super(counter, FourSight2.class.getName());
        this.solver = new Solver();
        this.moves = 0;
    }

    public int nbMoves() {
        return moves;
    }


    @Override
    public int makeMove(Board board) {
        long startTime = System.nanoTime();  // Start timing

        int bestMove = 4;  // Initialize with an invalid value
        int bestScore = Integer.MIN_VALUE;

        // Iterate through all possible columns to find the best move
        for (int x = 0; x < board.getConfig().getWidth(); x++) {
            if (solver.canPlay(board, x)) {
                try {
                    // Simulate the move by creating a new board
                    Board newBoard = new Board(board, x, this.getCounter());  // Use current player's counter
                    int score = solver.negamax(newBoard,this.getCounter(), Integer.MIN_VALUE, Integer.MAX_VALUE);  // Evaluate move

                    // Update bestMove if a better score is found
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = x;
                    }
                } catch (InvalidMoveException e) {
                    // Handle invalid move gracefully (shouldn't happen due to canPlay check)
                }
            }
        }

        // Increment move count for the player
        this.moves++;

        // Calculate and log the time taken to solve
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;  // Convert to milliseconds
        System.out.println("Best move: " + bestMove + " | Time taken: " + duration + " ms");

        return bestMove;  // Return the best move found
    }

}
