package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.Position;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class UtilityFunctions {
    public static Function<Integer, Integer> countToScore = count -> switch (count) {
        case 1 -> 10;
        case 2 -> 100;   // Increased for better early positioning
        case 3 -> 1000;  // Increased to be more aggressive
        case 4 -> 100000; // Much higher to ensure wins
        default -> 0;
    };

    public static Function<Integer, Integer> opponentCountToScore = count -> switch (count) {
        case 1 -> 15;
        case 2 -> 150;    // Higher blocking priority
        case 3 -> 2000;   // Much higher blocking priority
        case 4 -> 200000; // Must block wins
        default -> 0;
    };

    private static int spaceValue(Position position, Board board) {
        int height = board.getConfig().getHeight();
        int row = position.getY();
        // Prefer lower positions
        return (height - row) * 5;
    }

    public static int evaluateBoard(Board board, Counter counter) {
        int score = 0;
        boolean hasCenter = false;

        for (int i = 0; i < board.getConfig().getWidth(); i++) {
            for (int j = 0; j < board.getConfig().getHeight(); j++) {
                Position position = new Position(i, j);
                Counter currentCounter = board.getCounterAtPosition(position);

                if (currentCounter == counter && i == board.getConfig().getWidth() / 2) {
                    hasCenter = true;
                }

                if (currentCounter != null) {
                    int myScore = evaluatePosition(board, position, counter, countToScore);
                    int opponentScore = evaluatePosition(board, position, counter.getOther(), opponentCountToScore);
                    score += myScore - opponentScore + spaceValue(position, board);
                }

                // Evaluate empty spaces for potential moves
                if (currentCounter == null && isValidMove(board, i)) {
                    score += evaluatePotentialMove(board, position, counter);
                }
            }
        }

        return score + (hasCenter ? 30 : 0);
    }

    private static boolean isValidMove(Board board, int column) {
        return board.getCounterAtPosition(new Position(column, board.getConfig().getHeight() - 1)) == null;
    }

    private static int evaluatePotentialMove(Board board, Position position, Counter counter) {
        int score = 0;
        // Check if this position could complete a win
        if (couldCompleteWin(board, position, counter)) {
            score += 300;
        }
        if (couldCompleteWin(board, position, counter.getOther())) {
            score -= 400;
        }
        return score;
    }

    private static boolean couldCompleteWin(Board board, Position position, Counter counter) {
        return evaluateHorizontal(board, position, counter, count -> count >= 3 ? 1 : 0) > 0 ||
                evaluateVertical(board, position, counter, count -> count >= 3 ? 1 : 0) > 0 ||
                evaluateRightDiagonal(board, position, counter, count -> count >= 3 ? 1 : 0) > 0 ||
                evaluateLeftDiagonal(board, position, counter, count -> count >= 3 ? 1 : 0) > 0;
    }

    private static int evaluatePosition(Board board, Position position, Counter counter, Function<Integer, Integer> scoreFunction) {
        return evaluateHorizontal(board, position, counter, scoreFunction) +
                evaluateVertical(board, position, counter, scoreFunction) +
                evaluateRightDiagonal(board, position, counter, scoreFunction) +
                evaluateLeftDiagonal(board, position, counter, scoreFunction);
    }

    public static int evaluateHorizontal(Board board, Position position, Counter counter, Function<Integer, Integer> scoreFunction) {
        if (position.getX() < board.getConfig().getWidth() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count, position.getY())) == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }

    public static int evaluateVertical(Board board, Position position, Counter counter, Function<Integer, Integer> scoreFunction) {
        if (position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX(), position.getY() + count)) == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }

    public static int evaluateRightDiagonal(Board board, Position position, Counter counter, Function<Integer, Integer> scoreFunction) {
        if (position.getX() < board.getConfig().getWidth() - 3
                && position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count, position.getY() + count)) == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }

    public static int evaluateLeftDiagonal(Board board, Position position, Counter counter, Function<Integer, Integer> scoreFunction) {
        if (position.getX() >= 3
                && position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() - count, position.getY() + count)) == counter) {
                count++;
            }
            return scoreFunction.apply(count);
        }
        return 0;
    }
}
