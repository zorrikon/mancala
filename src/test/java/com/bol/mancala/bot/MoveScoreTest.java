package com.bol.mancala.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.bol.mancala.Player;

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

}
