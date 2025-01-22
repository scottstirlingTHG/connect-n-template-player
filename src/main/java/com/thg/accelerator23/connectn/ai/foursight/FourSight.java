package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.Player;
import com.thehutgroup.accelerator.connectn.player.Position;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Random;



public class FourSight extends Player {
  public FourSight(Counter counter) {
    //TODO: fill in your name here
    super(counter, FourSight.class.getName());
  }

  public List<Integer> findValidMovesColumns(Board board) {
    List<Integer> moves = new ArrayList<>();
    for (int i = 0; i < board.getConfig().getWidth(); i++){
      if (board.getCounterAtPosition(new Position(i, board.getConfig().getHeight() -1 )) == null){
        moves.add(i);
      }
    }
    return moves;
  }

  public int findOptimalMove(Board board, UtilityFunctions evaluateBoard) {

    int bestMove = 4;
    int bestScore = Integer.MIN_VALUE;
    for (int validMove : findValidMovesColumns(board)){
      int newScore = miniMax(board, 15, true, evaluateBoard, this.getCounter(), Integer.MIN_VALUE, Integer.MAX_VALUE);
      if (bestScore < newScore){
        bestScore = newScore;
        bestMove = validMove;
      }
    }
    return bestMove;
  }
  public int miniMax(Board board, int depth, boolean isMax, UtilityFunctions evaluateBoard, Counter counter, int alpha, int beta) {
    if (depth == 0) {
      return evaluateBoard(board);
    }

    if (isMax) {
      int maxEval = Integer.MIN_VALUE;
      for (int validMove : findValidMovesColumns(board)) {
        Board tempBoard = simulateMove(board, validMove);
        int eval = miniMax(tempBoard, depth - 1, false, evaluateBoard, counter, alpha, beta);
        maxEval = Math.max(maxEval, eval);
        alpha = Math.max(alpha, eval);
        if (beta <= alpha) {
          break;  // Beta cut-off
        }
      }
      return maxEval;
    } else {
      int minEval = Integer.MAX_VALUE;
      for (int validMove : findValidMovesColumns(board)) {
        Board tempBoard = simulateMove(board, validMove);
        int eval = miniMax(tempBoard, depth - 1, true, evaluateBoard, counter.getOther(), alpha, beta);
        minEval = Math.min(minEval, eval);
        beta = Math.min(beta, eval);
        if (beta <= alpha) {
          break;  // Alpha cut-off
        }
      }
      return minEval;
    }
  }



  @Override
  public int makeMove(Board board) {
    //TODO: some crazy analysis
    List<Integer> moves = findValidMovesColumns(board);
    Random rand = new Random();
    return moves.get(rand.nextInt(moves.size()));
    //TODO: make sure said analysis uses less than 2G of heap and returns within 10 seconds on whichever machine is running it

  }
}
