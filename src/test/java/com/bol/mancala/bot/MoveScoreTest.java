package com.bol.mancala.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.bol.mancala.GameState;
import com.bol.mancala.MancalaBoard.Player;

class MoveScoreTest {

	@Test
	void initialMoveScoresAreWorstForPlayer() {
		MoveScore initialForTop = MoveScore.initialFor(Player.TOP);
		MoveScore initialForBottom = MoveScore.initialFor(Player.BOTTOM);
		
		assertTrue(initialForTop.otherIsBetterForPlayer(initialForBottom, Player.TOP));
		assertTrue(initialForBottom.otherIsBetterForPlayer(initialForTop, Player.BOTTOM));
		
		assertFalse(initialForTop.otherIsBetterForPlayer(initialForBottom, Player.BOTTOM));
		assertFalse(initialForBottom.otherIsBetterForPlayer(initialForTop, Player.TOP));
	}

	@Test
	void initialMoveScoresAreWorseThanFinalForTop() {
		MoveScore initialForTop = MoveScore.initialFor(Player.TOP);
		MoveScore finalState = MoveScore.finalFor(GameState.initialState());
		

		assertTrue(initialForTop.otherIsBetterForPlayer(finalState, Player.TOP));
		assertTrue(finalState.otherIsBetterForPlayer(initialForTop, Player.BOTTOM));
		
		assertFalse(initialForTop.otherIsBetterForPlayer(finalState, Player.BOTTOM));
		assertFalse(finalState.otherIsBetterForPlayer(initialForTop, Player.TOP));
	}

	@Test
	void initialMoveScoresAreWorseThanFinalForBottom() {
		MoveScore initialForBottom = MoveScore.initialFor(Player.BOTTOM);
		MoveScore finalState = MoveScore.finalFor(GameState.initialState());
		

		assertTrue(initialForBottom.otherIsBetterForPlayer(finalState, Player.BOTTOM));
		assertTrue(finalState.otherIsBetterForPlayer(initialForBottom, Player.TOP));
		
		assertFalse(initialForBottom.otherIsBetterForPlayer(finalState, Player.TOP));
		assertFalse(finalState.otherIsBetterForPlayer(initialForBottom, Player.BOTTOM));
	}

}
