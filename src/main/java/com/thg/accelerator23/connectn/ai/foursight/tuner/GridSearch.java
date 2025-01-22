package com.thg.accelerator23.connectn.ai.foursight.tuner;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;

import java.util.Arrays;
import java.util.List;

public class GridSearch {

    public static void performGridSearch(Board board, Counter counter) {
        List<Integer> scoreFor1Values = Arrays.asList(1, 5, 10);
        List<Integer> scoreFor2Values = Arrays.asList(10, 30, 50);
        List<Integer> scoreFor3Values = Arrays.asList(50, 100, 150);
        List<Integer> scoreFor4Values = Arrays.asList(1000, 10000, 100000);
        List<Integer> horizontalWeightValues = Arrays.asList(1, 2, 3);
        List<Integer> verticalWeightValues = Arrays.asList(1, 2, 3);
        List<Integer> diagonalWeightValues = Arrays.asList(1, 2, 3);

        int bestScore = Integer.MIN_VALUE;
        int bestScoreFor1 = 0, bestScoreFor2 = 0, bestScoreFor3 = 0, bestScoreFor4 = 0;
        int bestHorizontalWeight = 0, bestVerticalWeight = 0, bestDiagonalWeight = 0;

        // Loop through all combinations
        for (int scoreFor1 : scoreFor1Values) {
            for (int scoreFor2 : scoreFor2Values) {
                for (int scoreFor3 : scoreFor3Values) {
                    for (int scoreFor4 : scoreFor4Values) {
                        for (int horizontalWeight : horizontalWeightValues) {
                            for (int verticalWeight : verticalWeightValues) {
                                for (int diagonalWeight : diagonalWeightValues) {
                                    // Call the objective function to evaluate the combination
                                    int currentScore = HyperparameterTuner.tuneHyperparameters(
                                            board, counter, scoreFor1, scoreFor2, scoreFor3, scoreFor4,
                                            horizontalWeight, verticalWeight, diagonalWeight);

                                    // Update the best parameters if this combination is better
                                    if (currentScore > bestScore) {
                                        bestScore = currentScore;
                                        bestScoreFor1 = scoreFor1;
                                        bestScoreFor2 = scoreFor2;
                                        bestScoreFor3 = scoreFor3;
                                        bestScoreFor4 = scoreFor4;
                                        bestHorizontalWeight = horizontalWeight;
                                        bestVerticalWeight = verticalWeight;
                                        bestDiagonalWeight = diagonalWeight;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Output the best parameters
        System.out.println("Best Score: " + bestScore);
        System.out.println("Best Hyperparameters: ");
        System.out.println("scoreFor1: " + bestScoreFor1);
        System.out.println("scoreFor2: " + bestScoreFor2);
        System.out.println("scoreFor3: " + bestScoreFor3);
        System.out.println("scoreFor4: " + bestScoreFor4);
        System.out.println("horizontalWeight: " + bestHorizontalWeight);
        System.out.println("verticalWeight: " + bestVerticalWeight);
        System.out.println("diagonalWeight: " + bestDiagonalWeight);
    }
}

