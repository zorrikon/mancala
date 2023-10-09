package com.bol.mancala;

// Maintains the pits and how many stones are in each.
public class MancalaBoard {
	private final int[] pits;
	
	static final int NUM_PITS_PER_PLAYER = 6;
	static final int NUM_LOCATIONS = (NUM_PITS_PER_PLAYER + 1) * 2;
	static final int INITIAL_STONES_PER_PIT = 6;
	
	public static MancalaBoard initialBoard() {
		return new MancalaBoard(new int[]{
		    INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT,
			INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT, 
			0, 
		    INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT,
			INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT, INITIAL_STONES_PER_PIT, 
			0});
	}
	
	// Used for testing, for normal situations create a board using initialBoard()
	public MancalaBoard(int[] pits) {
		this.pits = pits;
	}
	
	public MancalaBoard deepCopy() {
		return new MancalaBoard(pits.clone());
	}
	
	public int[] getPits() {
		return pits;
	}
	
	public static boolean isValidPitLocation(int pitLocation) {
		return 0 <= pitLocation && pitLocation < NUM_LOCATIONS;
	}
	
	public boolean isEmptyPit(int pitLocation) {
		return isValidPitLocation(pitLocation) && pits[pitLocation] == 0;
	}

	public static int getOppositeLocation(int pitLocation) {
		return NUM_LOCATIONS - 2 - pitLocation;
	}

	public int getNumStones(int pitLocation) {
		return pits[pitLocation];
	}

	public void addStone(int pitLocation) {
		pits[pitLocation] += 1;
	}

	public void clearLocation(int pitLocation) {
		pits[pitLocation] = 0;
	}

	public void moveStonesFromLocationToPlayerGoal(int pitLocation, Player player) {
		pits[player.getGoalLocation()] += pits[pitLocation];
		clearLocation(pitLocation);		
	}

	// Returns the next pit to place a stone. Skips over the opponent's goal.
	public static int getNextPitLocation(int pitLocation, Player activePlayer) {
		int nextPitLocation = (pitLocation + 1) % NUM_LOCATIONS; 
		if (activePlayer.otherPlayer().getGoalLocation() == nextPitLocation) {
			nextPitLocation = (nextPitLocation + 1) % NUM_LOCATIONS;
		}
		
		return nextPitLocation;
	}
	
}
