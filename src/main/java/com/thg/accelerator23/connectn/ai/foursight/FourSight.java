package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.Player;
import com.thehutgroup.accelerator.connectn.player.Position;

import java.util.ArrayList;
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


  @Override
  public int makeMove(Board board) {
    //TODO: some crazy analysis
    List<Integer> moves = findValidMovesColumns(board);
    Random rand = new Random();
    return moves.get(rand.nextInt(moves.size()));
    //TODO: make sure said analysis uses less than 2G of heap and returns within 10 seconds on whichever machine is running it

  }
}
