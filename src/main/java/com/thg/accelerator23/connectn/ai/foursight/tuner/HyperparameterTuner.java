package com.thg.accelerator23.connectn.ai.foursight.tuner;

import com.thg.accelerator23.connectn.ai.foursight.UtilityFunctions;
import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;

public class HyperparameterTuner {

    public static int tuneHyperparameters(Board board, Counter counter, int scoreFor1, int scoreFor2, int scoreFor3, int scoreFor4,
                                          int horizontalWeight, int verticalWeight, int diagonalWeight) {
        // Set the new hyperparameters dynamically
        UtilityFunctions.countToScore = count -> {
            if (count == 1) return scoreFor1;
            if (count == 2) return scoreFor2;
            if (count == 3) return scoreFor3;
            if (count == 4) return scoreFor4;
            return 0;
        };

        // Set direction weights dynamically
        UtilityFunctions.horizontalWeight = horizontalWeight;
        UtilityFunctions.verticalWeight = verticalWeight;
        UtilityFunctions.diagonalWeight = diagonalWeight;

        // Evaluate the board with the current hyperparameters
        int score = UtilityFunctions.evaluateBoard(board, counter);

        return score;  // Return the evaluation score or the result of some other metric
    }
}

