package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.*;

import java.lang.reflect.GenericDeclaration;
import java.sql.Timestamp;
import java.util.*;


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

    Map<String, Integer> transpositionTable = new HashMap<>();
    int bestMove = findValidMovesColumns(board).get(0);
    int bestScore = Integer.MIN_VALUE;

    for (int validMove : findValidMovesColumns(board)) {
      Board tempBoard = new Board(board, validMove, this.getCounter());
      int newScore = miniMax(tempBoard, 5, false, this.getCounter(), Integer.MIN_VALUE, Integer.MAX_VALUE, transpositionTable);
      if (bestScore < newScore) {
        bestScore = newScore;
        bestMove = validMove;
      }
    }
    Timestamp end = new Timestamp(System.currentTimeMillis());
    return bestMove;
  }

  public int miniMax(Board board, int depth, boolean isMax, Counter originalCounter, int alpha, int beta, Map<String, Integer> transpositionTable) throws InvalidMoveException {
    // Check transposition table
    String boardKey = board.toString() + "_" + depth + "_" + (isMax ? "max" : "min");
    if (transpositionTable.containsKey(boardKey)) {
      return transpositionTable.get(boardKey);
    }

    if (depth == 0) {
      int evaluation = UtilityFunctions.evaluateBoard(board, originalCounter);
      transpositionTable.put(boardKey, evaluation);
      return evaluation;
    }

    int bestEval = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    for (int validMove : findValidMovesColumns(board)) {
      Board tempBoard = new Board(board, validMove,
              isMax ? originalCounter : originalCounter.getOther());

      int eval = miniMax(tempBoard, depth - 1, !isMax, originalCounter, alpha, beta, transpositionTable);

      bestEval = isMax ?
              Math.max(bestEval, eval) :
              Math.min(bestEval, eval);

      if (isMax) {
        alpha = Math.max(alpha, eval);
      } else {
        beta = Math.min(beta, eval);
      }

      if (beta <= alpha) {
        break;
      }
    }

    // Cache the result
    transpositionTable.put(boardKey, bestEval);
    return bestEval;
  }
}
