package com.bol.mancala.bot;

import com.bol.mancala.GameState;
import com.bol.mancala.MancalaBoard.Player;

class MoveScore {
    final int move;
    final int score;
    
    static final int NULL_MOVE = -1;
    
    MoveScore(int move, int score) {
    	this.move = move;
    	this.score = score;
    }
    
    static MoveScore initialFor(Player player) {
    	return new MoveScore(NULL_MOVE, negativeInfinityScoreFor(player));
    }

	// Bottom wants to maximize the score, Top wants to minimize it.
    private static int negativeInfinityScoreFor(Player player) {
    	return player == Player.BOTTOM ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }
    
    static MoveScore finalFor(GameState gameState) {
    	return new MoveScore(NULL_MOVE, getScore(gameState));
    }

	private static int getScore(GameState gameState) {
		final int bottomScore = gameState.getBoard().getNumStones(Player.BOTTOM.getGoalLocation());
		final int topScore = gameState.getBoard().getNumStones(Player.TOP.getGoalLocation());
		return bottomScore - topScore;
	}
    
    boolean otherIsBetterForPlayer(MoveScore other, Player player) {
    	return player == Player.BOTTOM ? other.score > this.score : other.score < this.score;
    }

}
