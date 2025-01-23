package com.thg.accelerator23.connectn.ai.foursight;


import com.thehutgroup.accelerator.connectn.player.Counter;


import java.util.function.Function;


public class UtilityFunctions {
    public static Function<Integer, Integer> countToScore = count -> switch (count) {
        case 1 -> 10;
        case 2 -> 100;
        case 3 -> 1000;
        case 4 -> 100000;
        default -> 0;
    };

    public static Function<Integer, Integer> opponentCountToScore = count -> switch (count) {
        case 1 -> 15;
        case 2 -> 150;
        case 3 -> 2000;
        case 4 -> 200000;
        default -> 0;
    };

    public static int evaluateBoard(Counter[][] counterLocations, Counter counter) {
        int score = 0;
        boolean hasCenter = false;
        int width = counterLocations[0].length;
        int height = counterLocations.length;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (counterLocations[j][i] != null) {
                    if (counterLocations[j][i] == counter && i == width / 2) {
                        hasCenter = true;
                    }

                    int myScore = evaluatePositionInArray(counterLocations, j, i, counter, countToScore);
                    int opponentScore = evaluatePositionInArray(counterLocations, j, i, counter.getOther(), opponentCountToScore);
                    score += myScore - opponentScore + ((height - j) * 5);
                }
            }
        }

        return score + (hasCenter ? 30 : 0);
    }

    private static int evaluatePositionInArray(Counter[][] counterLocations, int row, int col, Counter counter, Function<Integer, Integer> scoreFunction) {
        return evaluateHorizontalInArray(counterLocations, row, col, counter, scoreFunction) +
                evaluateVerticalInArray(counterLocations, row, col, counter, scoreFunction) +
                evaluateRightDiagonalInArray(counterLocations, row, col, counter, scoreFunction) +
                evaluateLeftDiagonalInArray(counterLocations, row, col, counter, scoreFunction);
    }

    private static int evaluateHorizontalInArray(Counter[][] counterLocations, int row, int col, Counter counter, Function<Integer, Integer> scoreFunction) {
        int width = counterLocations[0].length;
        if (col < width - 3 && counterLocations[row][col] == counter) {
            int count = 1;
            while (count < 4 && col + count < width && counterLocations[row][col + count] == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }

    private static int evaluateVerticalInArray(Counter[][] counterLocations, int row, int col, Counter counter, Function<Integer, Integer> scoreFunction) {
        int height = counterLocations.length;
        if (row < height - 3 && counterLocations[row][col] == counter) {
            int count = 1;
            while (count < 4 && row + count < height && counterLocations[row + count][col] == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }

    private static int evaluateRightDiagonalInArray(Counter[][] counterLocations, int row, int col, Counter counter, Function<Integer, Integer> scoreFunction) {
        int width = counterLocations[0].length;
        int height = counterLocations.length;
        if (col < width - 3 && row < height - 3 && counterLocations[row][col] == counter) {
            int count = 1;
            while (count < 4 && col + count < width && row + count < height && counterLocations[row + count][col + count] == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }

    private static int evaluateLeftDiagonalInArray(Counter[][] counterLocations, int row, int col, Counter counter, Function<Integer, Integer> scoreFunction) {
        int width = counterLocations[0].length;
        int height = counterLocations.length;
        if (col >= 3 && row < height - 3 && counterLocations[row][col] == counter) {
            int count = 1;
            while (count < 4 && col - count >= 0 && row + count < height && counterLocations[row + count][col - count] == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }
}
