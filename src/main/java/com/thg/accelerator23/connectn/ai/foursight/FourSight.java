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

  public List<Integer> getAvailableCounterLocations(Counter[][] counterLocations){
    List<Integer> validLocations = new ArrayList<>();
    OuterLoop:
    for (int i = 0; i < counterLocations[0].length; i++) {
        for (Counter[] counterLocation : counterLocations) {
            if (counterLocation[i] == null) {
                validLocations.add(i);
                continue OuterLoop;
            }
        }
    }
    return validLocations;
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

  public static void addToColumn(Counter[][] counterLocations, int column, Counter counter) {
    for (int i = 0; i < counterLocations.length; i++) {
      if (counterLocations[i][column] == null) {
        counterLocations[i][column] = counter;
        break;
      }
    }
  }

  public static void removeFromColumn(Counter[][] counterLocations, int column) {
    for (int i = counterLocations.length - 1; i > -1; i--) {
      if (counterLocations[i][column] != null) {
        counterLocations[i][column] = null;
        break;
      }
    }
  }

  public int findOptimalMove(Board board) throws InvalidMoveException {
    Counter[][] counterLocations = board.getCounterPlacements();
    List<Integer> availableCounterLocations = getAvailableCounterLocations(counterLocations);
    int bestMove = availableCounterLocations.get(0);
    int bestScore = Integer.MIN_VALUE;

    for (int validMove : availableCounterLocations) {

        FourSight.addToColumn(counterLocations, validMove, this.getCounter());

        int newScore = miniMax(counterLocations, 7, false, this.getCounter().getOther(), Integer.MIN_VALUE, Integer.MAX_VALUE);
        FourSight.removeFromColumn(counterLocations, validMove);
      if (bestScore < newScore) {
        bestScore = newScore;
        bestMove = validMove;
      }
    }
    return bestMove;
  }

  public int miniMax(Counter[][] counterLocations, int depth, boolean isMax, Counter originalCounter, int alpha, int beta) throws InvalidMoveException {

    if (depth == 0) {
        return UtilityFunctions.evaluateBoard(counterLocations, originalCounter);
    }

    int bestEval = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    for (int validMove : getAvailableCounterLocations(counterLocations)) {
      FourSight.addToColumn(counterLocations, validMove, originalCounter);

      int eval = miniMax(counterLocations, depth - 1, !isMax, originalCounter.getOther(), alpha, beta);
      FourSight.removeFromColumn(counterLocations, validMove);

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

    return bestEval;
  }
}
