package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.*;

import java.util.ArrayList;

import java.util.List;



public class FourSight extends Player {
  public FourSight(Counter counter) {
    super(counter, FourSight.class.getName());
  }

  @Override
  public int makeMove(Board board) {
    try {
      return findOptimalMove(board);
    } catch (InvalidMoveException e) {
      return findValidMovesColumns(board).get(0);
    }
  }

  public List<Integer> findValidMovesColumns(Board board) {
    List<Integer> moves = new ArrayList<>();
    for (int i = 0; i < board.getConfig().getWidth(); i++) {
      if (board.getCounterAtPosition(new Position(i, board.getConfig().getHeight() - 1)) == null) {
        moves.add(i);
      }
    }
    return moves;
  }

  public int findOptimalMove(Board board) throws InvalidMoveException {
    int bestMove = findValidMovesColumns(board).get(0);
    int bestScore = Integer.MIN_VALUE;

    for (int validMove : findValidMovesColumns(board)) {
      Board tempBoard = new Board(board, validMove, this.getCounter());
      int newScore = miniMax(tempBoard, 6, false, this.getCounter(), Integer.MIN_VALUE, Integer.MAX_VALUE);
      if (bestScore < newScore) {
        bestScore = newScore;
        bestMove = validMove;
      }
    }
    return bestMove;
  }

  public int miniMax(Board board, int depth, boolean isMax, Counter originalCounter, int alpha, int beta) throws InvalidMoveException {
    if (depth == 0) {
      return UtilityFunctions.evaluateBoard(board, originalCounter);
    }

    if (isMax) {
      int maxEval = Integer.MIN_VALUE;
      for (int validMove : findValidMovesColumns(board)) {
        Board tempBoard = new Board(board, validMove, originalCounter);
        int eval = miniMax(tempBoard, depth - 1, false, originalCounter, alpha, beta);
        maxEval = Math.max(maxEval, eval);
        alpha = Math.max(alpha, eval);
        if (beta <= alpha) {
          break;
        }
      }
      return maxEval;
    } else {
      int minEval = Integer.MAX_VALUE;
      for (int validMove : findValidMovesColumns(board)) {
        Board tempBoard = new Board(board, validMove, originalCounter.getOther());
        int eval = miniMax(tempBoard, depth - 1, true, originalCounter, alpha, beta);
        minEval = Math.min(minEval, eval);
        beta = Math.min(beta, eval);
        if (beta <= alpha) {
          break;
        }
      }
      return minEval;
    }
  }
}