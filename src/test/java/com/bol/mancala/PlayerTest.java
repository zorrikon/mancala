package com.bol.mancala;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

	// otherPlayer
	
	@Test
	void testOtherPlayer() {
		assertEquals(Player.BOTTOM, Player.TOP.otherPlayer());
		assertEquals(Player.TOP, Player.BOTTOM.otherPlayer());
	}
	
	// getPitLocations
	
	@Test
	void testEachPlayerHasSixPitLocations() {
		assertEquals(MancalaBoard.NUM_PITS_PER_PLAYER, Player.BOTTOM.getPitLocations().size());
		assertEquals(MancalaBoard.NUM_PITS_PER_PLAYER, Player.TOP.getPitLocations().size());
	}

	@Test
	void testEachPlayerHasNonOverlappingPitLocations() {		
		assertTrue(Player.BOTTOM.getPitLocations().stream().noneMatch(
				pit -> Player.TOP.canPlayInPitLocation(pit)));
		
		assertTrue(Player.TOP.getPitLocations().stream().noneMatch(
				pit -> Player.BOTTOM.canPlayInPitLocation(pit)));
	}

	// canPlayPitLocation
	
	@Test
	void testEachPlayerCanPlayInTheirPits() {		
		assertTrue(Player.BOTTOM.getPitLocations().stream().allMatch(
				pit -> Player.BOTTOM.canPlayInPitLocation(pit)));
		
		assertTrue(Player.TOP.getPitLocations().stream().allMatch(
				pit -> Player.TOP.canPlayInPitLocation(pit)));
	}

	@Test
	void testPlayersCanNotPlayInGoals() {		
		assertFalse(Player.BOTTOM.canPlayInPitLocation(Player.BOTTOM.getGoalLocation()));
		assertFalse(Player.BOTTOM.canPlayInPitLocation(Player.TOP.getGoalLocation()));
		assertFalse(Player.TOP.canPlayInPitLocation(Player.TOP.getGoalLocation()));
		assertFalse(Player.TOP.canPlayInPitLocation(Player.BOTTOM.getGoalLocation()));
	}
	
	// Player.isGoalPit

	@Test
	void testGoalLocationsAreGoalPits() {		
		assertTrue(Player.isGoalPit(Player.BOTTOM.getGoalLocation()));
		assertTrue(Player.isGoalPit(Player.TOP.getGoalLocation()));
	}

	@Test
	void testPitLocationsAreNotGoalPits() {	
		assertTrue(Player.BOTTOM.getPitLocations().stream().noneMatch(pit -> Player.isGoalPit(pit)));
		assertTrue(Player.TOP.getPitLocations().stream().noneMatch(pit -> Player.isGoalPit(pit)));
	}
}
