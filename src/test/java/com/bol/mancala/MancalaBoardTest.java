package com.bol.mancala;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.bol.mancala.MancalaBoard.Player;

public class MancalaBoardTest {

	// deepCopy
	
	@Test
	void testModificationsToDeepCopyDoNotAffectOriginal() {
		MancalaBoard original = MancalaBoard.initialBoard();
		MancalaBoard deepCopy = original.deepCopy();
		
		int originalNumStones = original.getNumStones(3);
		
		deepCopy.addStone(3);
		
		assertEquals(originalNumStones, original.getNumStones(3));
		
		deepCopy.clearLocation(3);

		assertEquals(originalNumStones, original.getNumStones(3));
	}
	
	// initialBoard
	
	@Test
	void testModifyingInitialBoardDoesNotAffectOtherInitialBoards() {
		MancalaBoard initialBoard = MancalaBoard.initialBoard();
		initialBoard.addStone(2);
		
		MancalaBoard otherInitialBoard = MancalaBoard.initialBoard();
		
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, otherInitialBoard.getNumStones(2));
		
		initialBoard.addStone(3);
		
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, otherInitialBoard.getNumStones(3));
	}
	
	@Test
	void testInitialBoardHasEmptyGoalPits() {
		MancalaBoard initialBoard = MancalaBoard.initialBoard();
		
		assertEquals(0, initialBoard.getNumStones(Player.BOTTOM.getGoalLocation()));
		assertEquals(0, initialBoard.getNumStones(Player.TOP.getGoalLocation()));
	}
	
	// addStone
	
	@Test
	void testAddStone() {
		MancalaBoard board = MancalaBoard.initialBoard();
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, board.getNumStones(3));
		
		board.addStone(3);
		
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT + 1, board.getNumStones(3));
	}
	
	// clearLocation
	
	@Test
	void testClearLocation() {
		MancalaBoard board = MancalaBoard.initialBoard();
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, board.getNumStones(3));
		
		board.clearLocation(3);
		
		assertEquals(0, board.getNumStones(3));
		assertTrue(board.isEmptyPit(3));
	}
	
	// moveStonesFromLocationToPlayerGoal
	
	@Test
	void testMoveStonesFromLocationToPlayerGoalClearsAndAddsStonesToGoal() {
		MancalaBoard board = MancalaBoard.initialBoard();
		
		board.moveStonesFromLocationToPlayerGoal(3, Player.BOTTOM);
		
		assertEquals(0, board.getNumStones(3));
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, board.getNumStones(Player.BOTTOM.getGoalLocation()));
	}
	
	// MancalaBoard.getOppositeLocation
	
	@Test
	void testGetOppositeLocation() {
		List<Integer> locations = List.of(0, 1, 2, 3, 4, 5, 12, 11, 10, 9, 8, 7);
		List<Integer> opposites = List.of(12, 11, 10, 9, 8, 7, 0, 1, 2, 3, 4, 5);
		
		for (int i = 0; i < locations.size(); i++) {
			assertEquals(opposites.get(i), MancalaBoard.getOppositeLocation(locations.get(i)));
		}
	}
	
	
	// MancalaBoard.getNextPitLocation
	
	@Test
	void testGetNextPitLocationNormalLocations() {
		assertEquals(3, MancalaBoard.getNextPitLocation(2, Player.BOTTOM));
		assertEquals(3, MancalaBoard.getNextPitLocation(2, Player.TOP));
		assertEquals(9, MancalaBoard.getNextPitLocation(8, Player.BOTTOM));
		assertEquals(9, MancalaBoard.getNextPitLocation(8, Player.TOP));
	}
	
	@Test
	void testGetNextPitLocationSkipsOpponentsGoal() {
		assertEquals(13, Player.TOP.getGoalLocation());
		assertEquals(0, MancalaBoard.getNextPitLocation(12, Player.BOTTOM));

		assertEquals(6, Player.BOTTOM.getGoalLocation());
		assertEquals(7, MancalaBoard.getNextPitLocation(5, Player.TOP));
	}
	
	@Test
	void testGetNextPitLocationDoesNotSkipOwnGoal() {
		assertEquals(13, Player.TOP.getGoalLocation());
		assertEquals(13, MancalaBoard.getNextPitLocation(12, Player.TOP));

		assertEquals(6, Player.BOTTOM.getGoalLocation());
		assertEquals(6, MancalaBoard.getNextPitLocation(5, Player.BOTTOM));
	}
	
	// MancalaBoard.isValidPitLocation
	
	@Test
	void testNegativePitLocationsAreNotValid() {		
		assertFalse(MancalaBoard.isValidPitLocation(-1));
		assertFalse(MancalaBoard.isValidPitLocation(-5));
	}
	
	@Test
	void testLargePitLocationsAreNotValid() {		
		assertFalse(MancalaBoard.isValidPitLocation(MancalaBoard.NUM_LOCATIONS));
		assertFalse(MancalaBoard.isValidPitLocation(20));
	}
	
	@Test
	void testPitLocationsBetween0And13AreValid() {		
		assertTrue(MancalaBoard.isValidPitLocation(0));
		assertTrue(MancalaBoard.isValidPitLocation(5));
		assertTrue(MancalaBoard.isValidPitLocation(13));
	}

	// Player

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
