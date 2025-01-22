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
        case 2 -> 50;
        case 3 -> 500;
        case 4 -> 10000;
        default -> 0;
    };

    private static int centralColumnBias(Position position, Board board) {
        int centerColumn = board.getConfig().getWidth() / 2;
        return position.getX() == centerColumn ? 15 : 0;
    }

    public static int evaluateBoard(Board board, Counter counter) {
        int score = 0;
        for (int i = 0; i < board.getConfig().getWidth(); i++) {
            for (int j = 0; j < board.getConfig().getHeight(); j++) {
                Position position = new Position(i, j);
                int myScore = evaluateHorizontal(board, position, counter)
                        + evaluateVertical(board, position, counter)
                        + evaluateRightDiagonal(board, position, counter)
                        + evaluateLeftDiagonal(board, position, counter)
                        + centralColumnBias(position, board);

                int opponentScore = evaluateHorizontal(board, position, counter.getOther())
                        + evaluateVertical(board, position, counter.getOther())
                        + evaluateRightDiagonal(board, position, counter.getOther())
                        + evaluateLeftDiagonal(board, position, counter.getOther());

                score += myScore - opponentScore;
            }
        }
        return score;
    }

    public static int evaluateHorizontal(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getWidth() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count, position.getY())) == counter) {
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }

    public static int evaluateVertical(Board board, Position position, Counter counter) {
        if (position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX(), position.getY() + count)) == counter) {
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }

    public static int evaluateRightDiagonal(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getWidth() - 3
                && position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count, position.getY() + count)) == counter) {
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }

    public static int evaluateLeftDiagonal(Board board, Position position, Counter counter) {
        if (position.getX() >= 3
                && position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter) {
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() - count, position.getY() + count)) == counter) {
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }
}
