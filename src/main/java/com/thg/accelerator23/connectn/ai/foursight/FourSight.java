package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.*;

import java.util.ArrayList;

import java.util.List;



public class FourSight extends Player {
  public FourSight(Counter counter) {
    //TODO: fill in your name here
    super(counter, FourSight.class.getName());
  }

  @Override
  public int makeMove(Board board) {
    //TODO: some crazy analysis
    try {
      return this.findOptimalMove(board);
    } catch (InvalidMoveException e)  {
      return 4 ;
    }
    //TODO: make sure said analysis uses less than 2G of heap and returns within 10 seconds on whichever machine is running it

  }

  public List<Integer> findValidMovesColumns(Board board) {
    List<Integer> moves = new ArrayList<>();
    for (int i = 0; i < board.getConfig().getWidth(); i++){
      if (board.getCounterAtPosition(new Position(i, board.getConfig().getHeight() - 1 )) == null){
        moves.add(i);
      }
    }
    return moves;
  }



  public int findOptimalMove(Board board) throws InvalidMoveException {

    int bestMove = 4;
    int bestScore = Integer.MIN_VALUE;
    for (int validMove : findValidMovesColumns(board)){
      int newScore = miniMax(board, 16, true, this.getCounter(), Integer.MIN_VALUE, Integer.MAX_VALUE);
      if (bestScore < newScore){
        bestScore = newScore;
        bestMove = validMove;
      }
    }
    return bestMove;
  }


  public int miniMax(Board board, int depth, boolean isMax, Counter counter, int alpha, int beta) throws InvalidMoveException {
    if (depth == 0) {
      return UtilityFunctions.evaluateBoard(board, counter);
    }

    if (isMax) {
      int maxEval = Integer.MIN_VALUE;
      for (int validMove : findValidMovesColumns(board)) {
        Board tempBoard = new Board(board, validMove, counter);
        int eval = miniMax(tempBoard, depth - 1, false, counter, alpha, beta);
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
        Board tempBoard = new Board(board, validMove, counter.getOther());
        int eval = miniMax(tempBoard, depth - 1, true, counter.getOther(), alpha, beta);
        minEval = Math.min(minEval, eval);
        beta = Math.min(beta, eval);
        if (beta <= alpha) {
          break;  // Alpha cut-off
        }
      }
      return minEval;
    }
  }

}
