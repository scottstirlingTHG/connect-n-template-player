package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.Position;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class UtilityFunctions {



    BiFunction<Counter, Counter, Integer> parity = (counter1, counter2) -> {
        if (counter1.getStringRepresentation().equals(counter2.getStringRepresentation())){
            return 1;
        } else {
        return -1;
    }};

    Function<Integer, Integer> countToScore = count->  count ==  4 ? 10000000 : count;



    public int evaluateBoard(Board board, Counter counter) {
        int score = 0;
        for (int i = 0; i < board.getConfig().getWidth(); i++) {
            for (int j = 0; j < board.getConfig().getHeight(); j++) {
                score += countToScore.apply(evaluateHorizontal(new Position(i, j), counter))
                        + countToScore.apply(evaluateVertical(new Position(i, j), counter))
                        + countToScore.apply(evaluateRightDiagonal(new Position(i, j), counter));
            }
        }
        return score;
    }
}
