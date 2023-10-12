package com.bol.mancala.bot;

import java.util.List;

import com.bol.mancala.GameState;

public class MancalaBot {

	// Returns the move that maximizes the current player's lead in `depth` moves.
	// `depth` needs to be at least 1 to get a valid meaningful move.
	//
	// The runtime complexity grows exponentially with `depth` with run times of:
	//   ~0.05, 0.1, 0.2, 0.4 seconds for `depth`s of 5, 6, 7, 8, on my machine.
	public static int bestMove(GameState gameState, int depth) {
		return bestMoveAndScore(gameState, depth).move;
	}

	private static MoveScore bestMoveAndScore(GameState gameState, int depth) {
		if (depth <= 0 || gameState.isGameOver()) {
			return MoveScore.finalFor(gameState);
		}
		
		MoveScore best = MoveScore.initialFor(gameState.getActivePlayer());
		
		for (int pitLocation : getValidMoves(gameState)) {
			GameState gsCopy = gameState.deepCopy();
			gsCopy.doMove(pitLocation);
			MoveScore moveScore = bestMoveAndScore(gsCopy, depth - 1);
			if (best.otherIsBetterForPlayer(moveScore, gameState.getActivePlayer())) {
				best = new MoveScore(pitLocation, moveScore.score);
			}
		}
		
		return best;
	}
	
	private static List<Integer> getValidMoves(GameState gs) {
		return gs.getActivePlayer().getPitLocations().stream()
				.filter(pit -> gs.getInvalidMoveReason(pit).isEmpty())
				.toList();
	}
}
