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
        case 1 -> 10;  // low score for 1-in-a-row, but it's a potential starting point
        case 2 -> 50;  // stronger for 2-in-a-row, it's becoming a real threat
        case 3 -> 500; // even stronger for 3-in-a-row, close to winning
        case 4 -> 10000;  // large score for 4-in-a-row, actual win
        default -> 0;
    };



    public static int evaluateBoard(Board board, Counter counter) {
        int score = 0;
        for (int i = 0; i <= board.getConfig().getWidth(); i++) {
            for (int j = 0; j <= board.getConfig().getHeight(); j++) {
                score += evaluateHorizontal(board, new Position(i, j), counter)
                        + evaluateVertical(board, new Position(i, j), counter)
                        + evaluateRightDiagonal(board, new Position(i, j), counter)
                        + evaluateLeftDiagonal(board, new Position(i, j), counter);
            }
        }
        return score;
    }

    public static int evaluateHorizontal(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getWidth() - 3
                && board.getCounterAtPosition(position) == counter){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count, position.getY())) == counter){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }


    public static int evaluateVertical(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() , position.getY() + count)) == counter){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }

    public static int evaluateRightDiagonal(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getWidth() - 3
                && position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) ==counter ){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count , position.getY()+ count)) == counter){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }

    public static int evaluateLeftDiagonal(Board board, Position position, Counter counter) {
        if (position.getX() >= 3
                && position.getY() < board.getConfig().getHeight() - 3
                && board.getCounterAtPosition(position) == counter){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() - count , position.getY()+ count)) == counter){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }


}
