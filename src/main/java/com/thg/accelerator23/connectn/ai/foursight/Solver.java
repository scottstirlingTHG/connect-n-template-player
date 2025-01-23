package com.thg.accelerator23.connectn.ai.foursight;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.InvalidMoveException;
import com.thehutgroup.accelerator.connectn.player.Position;

public class Solver {

    private long nodeCount = 0;

    public boolean canPlay(Board board, int col) {
        // A column is playable if there is an empty row in the column
        return board.getCounterAtPosition(new Position(col, board.getConfig().getHeight() - 1)) == null;
    }



    // Check if the board is full (no more moves possible)
    public boolean isFull(Board board) {
        for (int col = 0; col < board.getConfig().getWidth(); col++) {
            if (canPlay(board, col)) {
                return false;  // Found an empty space
            }
        }
        return true;  // No empty spaces left
    }

    public int negamax(Board board, Counter counter, int alpha, int beta) {
        nodeCount++;

        // Check if the board is full (draw case)
        if (isFull(board)) {
            return 0;  // Draw case
        }

        // Check for winning moves for the current player
        for (int x = 0; x < board.getConfig().getWidth(); x++) {
            if (canPlay(board, x) && isWinningMove(board, x)) {
                return Integer.MAX_VALUE;  // Positive score for winning
            }
        }

        // Apply alpha-beta pruning
        for (int x = 0; x < board.getConfig().getWidth(); x++) {
            if (canPlay(board, x)) {
                try{
                Board newBoard = new Board(board, x, counter);
                int score = -negamax(newBoard, counter,  -beta, -alpha);  // Negate the score for the opponent's turn

                if (score >= beta) {
                    return score;
                }
                if (score > alpha) {
                    alpha = score;
                }} catch(InvalidMoveException e) {
                    continue;
                }
            }
        }
        return alpha;  // Return the best score found
    }


    // Check for winning moves (vertical, horizontal, diagonal)
    public boolean isWinningMove(Board board, int col) {
        Counter currentPlayer = board.getCounterPlacements()[col][board.getConfig().getHeight() - 1];

        // Get the row where the counter would be placed
        int row = getMinVacantY(board, col);

        return checkVertical(board, col, row, currentPlayer) ||
                checkHorizontal(board, row, currentPlayer) ||
                checkDiagonals(board, col, row, currentPlayer);
    }

    private int getMinVacantY(Board board, int col) {
        for (int row = 0; row < board.getConfig().getHeight(); row++) {
            if (board.getCounterAtPosition(new Position(col, row)) == null) {
                return row;
            }
        }
        return -1;  // Column is full
    }

    private boolean checkVertical(Board board, int col, int row, Counter player) {
        int count = 0;
        for (int i = row; i >= 0; i--) {
            if (board.getCounterAtPosition(new Position(col, i)) == player) {
                count++;
            } else {
                break;
            }
        }
        return count >= 4;
    }

    private boolean checkHorizontal(Board board, int row, Counter player) {
        int count = 0;
        int width = board.getConfig().getWidth();
        for (int i = -3; i <= 3; i++) {
            int col = i + row;
            if (col >= 0 && col < width && board.getCounterAtPosition(new Position(col, row)) == player) {
                count++;
            }
        }
        return count >= 4;
    }

    private boolean checkDiagonals(Board board, int col, int row, Counter player) {
        // Positive slope (bottom-left to top-right)
        int count1 = 0;
        for (int i = -3; i <= 3; i++) {
            int x = col + i;
            int y = row + i;
            if (board.isWithinBoard(new Position(x, y)) && board.getCounterAtPosition(new Position(x, y)) == player) {
                count1++;
            }
        }
        if (count1 >= 4) return true;

        // Negative slope (top-left to bottom-right)
        int count2 = 0;
        for (int i = -3; i <= 3; i++) {
            int x = col + i;
            int y = row - i;
            if (board.isWithinBoard(new Position(x, y)) && board.getCounterAtPosition(new Position(x, y)) == player) {
                count2++;
            }
        }

        return count2 >= 4;
    }

    public long getNodeCount() {
        return nodeCount;
    }
}
