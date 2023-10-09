package com.bol.mancala;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class GameStateTest {

	public static void assertGameStateEquals(GameState a, GameState b) {
		assertEquals(a.getActivePlayer(), b.getActivePlayer());
		assertArrayEquals(a.getBoard().getPits(), b.getBoard().getPits());
	}
	
	// deepCopy
	
	@Test
	void testModifyingDeepCopyDoesNotAffectOriginal() {
		GameState original = GameState.initialState();
		GameState deepCopy = original.deepCopy();
		
		deepCopy.doMove(3);
		
		assertEquals(Player.TOP, deepCopy.getActivePlayer());
		assertEquals(0, deepCopy.getBoard().getNumStones(3));
		
		assertEquals(Player.BOTTOM, original.getActivePlayer());
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, original.getBoard().getNumStones(3));
		
		assertGameStateEquals(GameState.initialState(), original);
	}
	
	// initialState

	@Test
	void testModifyingGameStateDoesNotAffectInitialState() {
		GameState board = GameState.initialState();
		
		board.doMove(3);
		
		GameState initialState = GameState.initialState();
		
		board.doMove(8);
						
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, initialState.getBoard().getNumStones(3));
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT, initialState.getBoard().getNumStones(8));
		
		assertGameStateEquals(GameState.initialState(), initialState);
	}
	
	// getInvalidMoveReason
	
	@Test
	void testCanMakeNormalMove() {
		GameState gameState = GameState.initialState();

		Optional<String> invalidMoveReason = gameState.getInvalidMoveReason(3);
		
		assertFalse(invalidMoveReason.isPresent());
	}
	
	@Test
	void testCannotMovePitOutOfBounds() {
		GameState gameState = GameState.initialState();

		{
			Optional<String> invalidMoveReason = gameState.getInvalidMoveReason(-1);
			
			assertTrue(invalidMoveReason.isPresent());
			assertEquals("Invalid move.", invalidMoveReason.get());
		}

		{
			Optional<String> invalidMoveReason = gameState.getInvalidMoveReason(MancalaBoard.NUM_LOCATIONS);
			
			assertTrue(invalidMoveReason.isPresent());
			assertEquals("Invalid move.", invalidMoveReason.get());
		}
	}
	
	@Test
	void testCannotMoveOpponentPit() {
		GameState gameState = GameState.initialState();

		Optional<String> invalidMoveReason = gameState.getInvalidMoveReason(8);
		
		assertTrue(invalidMoveReason.isPresent());
		assertEquals("You can't play from your opponent's pits.", invalidMoveReason.get());
	}
	
	@Test
	void testCannotMoveFromGoal() {
		GameState gameState = GameState.initialState();

		assertTrue(gameState.getInvalidMoveReason(gameState.getActivePlayer().getGoalLocation()).isPresent());
		assertTrue(gameState.getInvalidMoveReason(
				gameState.getActivePlayer().otherPlayer().getGoalLocation()).isPresent());
	}
	
	@Test
	void testCannotMoveFromEmptyPit() {
		GameState gameState = GameState.initialState();

		gameState.getBoard().clearLocation(3);
		Optional<String> invalidMoveReason = gameState.getInvalidMoveReason(3);
		
		assertTrue(invalidMoveReason.isPresent());
		assertEquals("You can't play from an empty pit.", invalidMoveReason.get());
	}
	
	// doMove
	
	@Test
	void testInvalidMovesDoNotAffectGameState() {
		GameState gameState = GameState.initialState();

		{
			String message = gameState.doMove(8);
			
			assertGameStateEquals(GameState.initialState(), gameState);
			assertFalse(message.isEmpty());
		}

		{
			String message = gameState.doMove(Player.BOTTOM.getGoalLocation());
			
			assertGameStateEquals(GameState.initialState(), gameState);
			assertFalse(message.isEmpty());
		}
	}
	
	@Test
	void testDoMoveEmptiesPitAndAddsStonesToSubsequentPits() {
		GameState gameState = GameState.initialState();
		
		gameState.doMove(1);

		assertEquals(0, gameState.getBoard().getNumStones(1));
		assertEquals(7, gameState.getBoard().getNumStones(2));
		assertEquals(7, gameState.getBoard().getNumStones(3));
		assertEquals(7, gameState.getBoard().getNumStones(4));
		assertEquals(7, gameState.getBoard().getNumStones(5));
		assertEquals(1, gameState.getBoard().getNumStones(6));
		assertEquals(7, gameState.getBoard().getNumStones(7));
	}
	
	@Test
	void testGettingTheFinalStoneInYourGoalGivesYouAnExtraTurn() {
		GameState gameState = GameState.initialState();
		String message = gameState.doMove(0);
		
		assertEquals(GameState.TAKE_ANOTHER_TURN_MESSAGE, message);
		assertEquals(Player.BOTTOM, gameState.getActivePlayer());
	}
	
	@Test
	void testLandingOnAnEmptySpotOnYourSideCapturesYourStoneAndTheOpponents() {
		GameState gameState = GameState.initialState();
		gameState.getBoard().clearLocation(5);
		gameState.getBoard().getPits()[0] = 5;
		
		String message = gameState.doMove(0);
				
		assertTrue(gameState.getBoard().isEmptyPit(5));
		assertTrue(gameState.getBoard().isEmptyPit(7));
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT + 1, gameState.getBoard().getNumStones(6));
		assertEquals(GameState.CAPTURED_OPPONENTS_STONE_MESSAGE, message);
		assertEquals(Player.TOP, gameState.getActivePlayer());
	}
	
	@Test
	void testLandingOnAnEmptySpotOnOpponentsSideDoesNotCaptureStones() {
		GameState gameState = GameState.initialState();
		gameState.getBoard().clearLocation(7);
		
		String message = gameState.doMove(1);
				
		assertEquals(MancalaBoard.INITIAL_STONES_PER_PIT + 1, gameState.getBoard().getNumStones(5));
		assertEquals(1, gameState.getBoard().getNumStones(7));
		assertEquals(1, gameState.getBoard().getNumStones(6));
		assertEquals("", message);
		assertEquals(Player.TOP, gameState.getActivePlayer());
	}
	
	@Test
	void testDoesNotAddStonesToOpponentsGoal() {
		GameState gameState = GameState.initialState();
		gameState.getBoard().getPits()[0] = 50;
		
		gameState.doMove(0);

		assertEquals(0, gameState.getBoard().getNumStones(13));
	}
	
	@Test
	void testDoMoveFinishesGame() {
		int[] almostFinishedPits = {5,0,0,0,0,0, 0, 3,0,0,0,0,0, 7};
		int[] expectedFinalPits  = {0,0,0,0,0,0, 8, 0,0,0,0,0,0, 7};
		
		GameState gameState = new GameState(new MancalaBoard(almostFinishedPits), Player.BOTTOM);

		assertFalse(gameState.isGameOver());
		
		String message = gameState.doMove(0);

		assertTrue(gameState.isGameOver());
		assertEquals("Bottom player wins!", message);
		assertArrayEquals(expectedFinalPits, gameState.getBoard().getPits());
	}

	// isGameOver
	
	@Test
	void testInitialGameIsNotOver() {
		GameState initialGameState = GameState.initialState();
		
		assertFalse(initialGameState.isGameOver());
	}
	
	@Test
	void testEmptyBottomBoardIsOver() {
		GameState gameState = GameState.initialState();
		Player.BOTTOM.getPitLocations().forEach(pit -> gameState.getBoard().clearLocation(pit));
		
		assertTrue(gameState.isGameOver());
	}
	
	@Test
	void testEmptyTopBoardIsOver() {
		GameState gameState = GameState.initialState();
		Player.TOP.getPitLocations().forEach(pit -> gameState.getBoard().clearLocation(pit));
		
		assertTrue(gameState.isGameOver());
	}
	
	// getGameResultMessage
	
	@Test
	void testGameResultMessageBottomWins() {
		int[] pits = {0,0,0,0,0,0, 8, 0,0,0,0,0,0, 7};
		GameState gameState = new GameState(new MancalaBoard(pits), Player.BOTTOM);

		assertTrue(gameState.isGameOver());

		assertEquals("Bottom player wins!", gameState.getGameResultMessage());
	}
	
	@Test
	void testGameResultMessageTopWins() {
		int[] pits = {0,0,0,0,0,0, 8, 0,0,0,0,0,0, 9};
		GameState gameState = new GameState(new MancalaBoard(pits), Player.BOTTOM);

		assertTrue(gameState.isGameOver());

		assertEquals("Top player wins!", gameState.getGameResultMessage());
	}
	
	@Test
	void testGameResultMessageTie() {
		int[] pits = {0,0,0,0,0,0, 8, 0,0,0,0,0,0, 8};
		GameState gameState = new GameState(new MancalaBoard(pits), Player.BOTTOM);

		assertTrue(gameState.isGameOver());

		assertEquals("Tie!", gameState.getGameResultMessage());
	}
}
