package com.thg.accelerator23.connectn.ai.foursight;


import com.thehutgroup.accelerator.connectn.player.Counter;


import java.util.function.Function;


public class UtilityFunctions {

    public static Function<Integer, Integer> countToScore = count -> switch (count) {
        case 2 -> 50;
        case 3 -> 1000;
        case 4 -> 100000;
        default -> 0;
    };

    public static Function<Integer, Integer> opponentCountToScore = count -> switch (count) {
        case 2 -> 100;
        case 3 -> 5000;
        case 4 -> 200000;
        default -> 0;
    };

    public static int evaluateBoard(Counter[][] counterLocations, Counter counter) {
        int score = 0;
        int width = counterLocations.length;
        int height = counterLocations[0].length;
        boolean hasCenter = false;

        // Positional heuristic: center control is important
        int centerColumn = width / 2;

        // Weigh columns in terms of their distance to the center
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (counterLocations[x][y] != null) {
                    if (counterLocations[x][y] == counter && x == centerColumn) {
                        hasCenter = true;
                    }

                    int myScore = evaluatePositionInArray(counterLocations, x, y, counter, countToScore);
                    int opponentScore = evaluatePositionInArray(counterLocations, x, y, counter.getOther(), opponentCountToScore);

                    // Apply the base score and give bonus for controlling the center (closeness to the center row is also important)
                    score += myScore - opponentScore + ((height - y) * 5);  // Reward pieces closer to the bottom

                    // Add positional bonus for central columns
                    if (x == centerColumn) {
                        score += 10;
                    }
                }
            }
        }

        // Additional bonus for controlling the center (adjustable)
        return score + (hasCenter ? 20 : 0);
    }

    private static int evaluatePositionInArray(Counter[][] counterLocations, int col, int row, Counter counter,
                                               Function<Integer, Integer> scoreFunction) {
        int score = 0;

        // Evaluate horizontal, vertical, and diagonal lines
        score += evaluateLine(counterLocations, col, row, counter, scoreFunction, "horizontal");
        score += evaluateLine(counterLocations, col, row, counter, scoreFunction, "vertical");
        score += evaluateLine(counterLocations, col, row, counter, scoreFunction, "rightDiagonal");
        score += evaluateLine(counterLocations, col, row, counter, scoreFunction, "leftDiagonal");

        return score;
    }

    private static int evaluateLine(Counter[][] counterLocations, int col, int row, Counter counter,
                                    Function<Integer, Integer> scoreFunction, String direction) {
        int count = 1;
        int width = counterLocations.length;
        int height = counterLocations[0].length;

        switch (direction) {
            case "horizontal":
                if (col < width - 3 && counterLocations[col][row] == counter) {
                    while (count < 4 && col + count < width && counterLocations[col + count][row] == counter) {
                        count++;
                    }
                    return scoreFunction.apply(count);
                }
                break;
            case "vertical":
                if (row < height - 3 && counterLocations[col][row] == counter) {
                    while (count < 4 && row + count < height && counterLocations[col][row + count] == counter) {
                        count++;
                    }
                    return scoreFunction.apply(count);
                }
                break;
            case "rightDiagonal":
                if (col < width - 3 && row < height - 3 && counterLocations[col][row] == counter) {
                    while (count < 4 && col + count < width && row + count < height && counterLocations[col + count][row + count] == counter) {
                        count++;
                    }
                    return scoreFunction.apply(count);
                }
                break;
            case "leftDiagonal":
                if (col >= 3 && row < height - 3 && counterLocations[col][row] == counter) {
                    while (count < 4 && col - count >= 0 && row + count < height && counterLocations[col - count][row + count] == counter) {
                        count++;
                    }
                    return scoreFunction.apply(count);
                }
                break;
        }
        return 0;
    }

    // Look for sequences that are close to completing (e.g., two-in-a-row or three-in-a-row)
    private static int evaluatePartialSequences(Counter[][] counterLocations, int col, int row, Counter counter) {
        int score = 0;
        int width = counterLocations.length;
        int height = counterLocations[0].length;

        // Evaluate for potential 2-in-a-row or 3-in-a-row
        int[] sequenceValues = {0, 1, 2, 3};  // Look for 2-in-a-row, 3-in-a-row, etc.

        for (int sequence : sequenceValues) {
            // Example for horizontal evaluation of 2-in-a-row
            if (col < width - 3 && counterLocations[col][row] == counter) {
                int count = 1;
                while (count < 4 && col + count < width && counterLocations[col + count][row] == counter) {
                    count++;
                }
                if (count == 2) {
                    score += 50;  // Reward for creating 2-in-a-row
                } else if (count == 3) {
                    score += 1000;  // Reward for creating 3-in-a-row
                }
            }
        }
        return score;
    }
}


