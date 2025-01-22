package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.Position;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class UtilityFunctions {





    public static Function<Integer, Integer>  countToScore = count ->{
        if (count == 1){
            return 1;
        }
        if (count == 2){
            return 20;
        }
        if (count == 3){
            return 60;
        }
        if (count ==  4){
            return 1000000000;
        }
        return 0;
    };



    public static int evaluateBoard(Board board, Counter counter) {
        int score = 0;
        for (int i = 0; i < board.getConfig().getWidth() - 4; i++) {
            for (int j = 0; j < board.getConfig().getHeight()- 4; j++) {
                score += evaluateHorizontal(board, new Position(i, j), counter)
                        + evaluateVertical(board, new Position(i, j), counter)
                        + evaluateRightDiagonal(board, new Position(i, j), counter)
                        + evaluateLeftDiagonal(board, new Position(i, j), counter);
            }
        }
        return score;
    }

    public static int evaluateHorizontal(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getWidth() - 4
                && board.getCounterAtPosition(position).equals(counter)){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count, position.getY())).equals(counter)){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }


    public static int evaluateVertical(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getHeight() - 4
                && board.getCounterAtPosition(position).getStringRepresentation().equals(counter.getStringRepresentation())){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() , position.getY() + count)).equals(counter)){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }

    public static int evaluateRightDiagonal(Board board, Position position, Counter counter) {
        if (position.getX() < board.getConfig().getWidth() - 4
                && position.getY() < board.getConfig().getHeight() - 4
                && board.getCounterAtPosition(position).getStringRepresentation().equals(counter.getStringRepresentation())){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() + count , position.getY()+ count)).equals(counter)){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }

    public static int evaluateLeftDiagonal(Board board, Position position, Counter counter) {
        if (position.getX() >= 3
                && position.getY() < board.getConfig().getHeight() - 4
                && board.getCounterAtPosition(position).getStringRepresentation().equals(counter.getStringRepresentation())){
            int count = 1;
            while (count < 4 && board.getCounterAtPosition(new Position(position.getX() - count , position.getY()+ count)).equals(counter)){
                count++;
            }
            return countToScore.apply(count);
        }
        return 0;
    }


}
