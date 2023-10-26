package com.bol.mancala.bot;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.bol.mancala.GameState;
import com.bol.mancala.GameStateTest;
import com.bol.mancala.MancalaBoard;
import com.bol.mancala.MancalaBoard.Player;

class MancalaBotTest {

	@Test
	void testBestMoveDoesNotModifyGameState() {
		GameState gameState = GameState.initialState();

		MancalaBot.bestMove(gameState, 2);
		
		GameStateTest.assertGameStateEquals(GameState.initialState(), gameState);
	}
	
	@Test
	void testProvidedMoveShouldBeValid() {
		GameState gameState = GameState.initialState();
		
		int bestMove = MancalaBot.bestMove(gameState, 1);
		Optional<String> invalidReason = gameState.getInvalidMoveReason(bestMove);
		
		assertTrue(invalidReason.isEmpty());		
	}

	@Test
	void testProvidedMoveShouldBeValidForSecondPlayer() {
		GameState gameState = new GameState(MancalaBoard.initialBoard(), Player.TOP);	

		int bestMove = MancalaBot.bestMove(gameState, 1);
		Optional<String> invalidReason = gameState.getInvalidMoveReason(bestMove);
		
		assertTrue(invalidReason.isEmpty());		
	}

	@Test
	void testProvidedMoveShouldBeMaximizeScoreForDepth2() {
		GameState gameState = GameState.initialState();
		
		// All other moves pass the turn and thus let the opponent score on the second turn.
		assertEquals(0, MancalaBot.bestMove(gameState, 2)); 
	}

	@Test
	void testProvidedMoveShouldCaptureBiggerPile() {
		int[] canCaptureBiggerPileByMovingPit0 = {2,0,0,0,1,0, 0, 3,0,0,5,0,0, 0};
		GameState gameState = new GameState(new MancalaBoard(canCaptureBiggerPileByMovingPit0),
											Player.BOTTOM);		
	
		assertEquals(0, MancalaBot.bestMove(gameState, 2));
	}

	@Test
	void testProvidesBestMoveForSecondPlayerToo() {
		int[] canCaptureByMovingPit7 = {0,0,0,5,0,0, 0, 2,0,0,0,1,0, 0};
		GameState gameState = new GameState(new MancalaBoard(canCaptureByMovingPit7), Player.TOP);	

		assertEquals(7, MancalaBot.bestMove(gameState, 2));
	}

	@Test
	void testBestMoveDoesNotCrashForDepth5() {
		GameState gameState = GameState.initialState();

		int bestMove = MancalaBot.bestMove(gameState, 5);
		Optional<String> invalidReason = gameState.getInvalidMoveReason(bestMove);
		
		assertTrue(invalidReason.isEmpty());		
	}
	
	@Test
	void testDoesNotCrashForAlreadyFinishedGame() {
		int[] finishedGamePits = {0,0,0,0,0,0, 35, 0,0,0,0,0,0, 37};
		GameState gameState = new GameState(new MancalaBoard(finishedGamePits), Player.TOP);
		
		MancalaBot.bestMove(gameState, 5); // Doesn't crash	
	}
	
	@Test
	void testDoesNotCrashByFinishingGame() {
		int[] almostFinishedGamePits = {0,0,0,0,0,1, 35, 0,0,0,5,0,0, 37};
		GameState gameState = new GameState(new MancalaBoard(almostFinishedGamePits), Player.BOTTOM);

		assertEquals(5, MancalaBot.bestMove(gameState, 5)); // Doesn't crash	
	}

}
