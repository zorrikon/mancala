package com.bol.mancala;

import java.util.Optional;
import java.util.stream.Stream;

// Maintains the game state (the Mancala board and whose turn it is). 
public class GameState {
	
	static final String INVALID_MOVE_MESSAGE = "Invalid move.";
	static final String INVALID_OPPONENT_PIT_MOVE_MESSAGE = "You can't play from your opponent's pits.";
	static final String INVALID_EMPTY_PIT_MOVE_MESSAGE = "You can't play from an empty pit.";
	// TODO: Modify message to still be valid when the opposite location was empty (none of the opponent's
	// 	stones were actually captured).
	static final String CAPTURED_OPPONENTS_STONE_MESSAGE = "Opponent's stones captured!";
	static final String TAKE_ANOTHER_TURN_MESSAGE = "Take another turn!";
	
	private final MancalaBoard board;
	
	private Player activePlayer;
	
	public static GameState initialState() {
		return new GameState(MancalaBoard.initialBoard(), Player.BOTTOM);
	}
	
	// Used for testing, for normal situations create a GameState with initialState().
	public GameState(MancalaBoard board, Player activePlayer) {
		this.board = board;
		this.activePlayer = activePlayer;
	}
	
	public GameState deepCopy() {
		return new GameState(board.deepCopy(), activePlayer);
	}
	
	public Player getActivePlayer() {
		return activePlayer;
	}
	
	public MancalaBoard getBoard() {
		return board;
	}
	
	// Changes `this` by implementing the move at pitLocation. If the move is invalid, `this` isn't changed.
	// The returned string is a message for the user.
	public String doMove(int pitLocation) {
		Optional<String> invalidReason = getInvalidMoveReason(pitLocation);
		if (invalidReason.isPresent()) {
			return invalidReason.get();
		}
		
		int numStones = board.getNumStones(pitLocation);
		board.clearLocation(pitLocation);
		while (numStones > 0) {
			pitLocation = MancalaBoard.getNextPitLocation(pitLocation, activePlayer);
			board.addStone(pitLocation);
			numStones--;
		}
		
		String message = "";
		
		if (board.getNumStones(pitLocation) == 1 && activePlayer.canPlayInPitLocation(pitLocation)) {
			final int oppositeLocation = MancalaBoard.getOppositeLocation(pitLocation);
			board.moveStonesFromLocationToPlayerGoal(pitLocation, activePlayer);
			board.moveStonesFromLocationToPlayerGoal(oppositeLocation, activePlayer);
			message = CAPTURED_OPPONENTS_STONE_MESSAGE;
		}
		
		if (activePlayer.getGoalLocation() == pitLocation) {
			message = TAKE_ANOTHER_TURN_MESSAGE;
		} else {
			activePlayer = activePlayer.otherPlayer();
		}
		
		if (isGameOver()) {
			scoreEachPlayersStones();
			message = getGameResultMessage();
		}
		
		return message;
	}
	
	// Returns a message for why the move is invalid or an empty optional if the move is valid
	public Optional<String> getInvalidMoveReason(int pitLocation) {
		if (!MancalaBoard.isValidPitLocation(pitLocation) || Player.isGoalPit(pitLocation)) {
			return Optional.of(INVALID_MOVE_MESSAGE);
		}
		if (!activePlayer.canPlayInPitLocation(pitLocation)) {
			return Optional.of(INVALID_OPPONENT_PIT_MOVE_MESSAGE); 
		}
		if (board.isEmptyPit(pitLocation)) {
			return Optional.of(INVALID_EMPTY_PIT_MOVE_MESSAGE);
		}
		return Optional.empty();
	}

	public boolean isGameOver() {
		return Stream.of(Player.values()).anyMatch(
				player -> player.getPitLocations().stream().allMatch(pit -> board.isEmptyPit(pit)));
	}

	private void scoreEachPlayersStones() {
		for (Player player : Player.values()) {
			player.getPitLocations().forEach(pit -> board.moveStonesFromLocationToPlayerGoal(pit, player));
		}
		
	}
	
	public String getGameResultMessage() {
		final int bottomScore = board.getNumStones(Player.BOTTOM.getGoalLocation());
		final int topScore = board.getNumStones(Player.TOP.getGoalLocation());
		if (bottomScore == topScore) {
			return "Tie!";
		}
		return (bottomScore > topScore ? Player.BOTTOM.getName() : Player.TOP.getName()) + " player wins!";
	}
}
