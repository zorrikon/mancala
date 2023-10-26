package com.bol.mancala;

import java.util.List;
import java.util.stream.IntStream;

// Maintains the pits and how many stones are in each.
public class MancalaBoard {
	private final int[] pits;
	
	static final int NUM_PITS_PER_PLAYER = 6;
	static final int NUM_LOCATIONS = (NUM_PITS_PER_PLAYER + 1) * 2;
	static final int INITIAL_STONES_PER_PIT = 6;
	
	public enum Player {
		BOTTOM("Bottom", 0, NUM_PITS_PER_PLAYER),
		TOP("Top", NUM_PITS_PER_PLAYER + 1, NUM_PITS_PER_PLAYER * 2 + 1);

		private final String name;
		private final int firstPit;
		private final int goalLocation;
		
		private Player(String name, int firstPit, int goalLocation) {
			this.name = name;
			this.firstPit = firstPit;
			this.goalLocation = goalLocation;
		}
		
		public Player otherPlayer() {
			return this == TOP ? BOTTOM : TOP;
		}
		
		public boolean canPlayInPitLocation(int pitLocation) {
			return this.firstPit <= pitLocation && pitLocation < this.goalLocation; 
		}
		
		public List<Integer> getPitLocations() {
			return IntStream.range(this.firstPit, this.goalLocation).boxed().toList();
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getGoalLocation() {
			return this.goalLocation;
		}

		public static boolean isGoalPit(int pitLocation) {
			return pitLocation == BOTTOM.getGoalLocation() || pitLocation == TOP.getGoalLocation();
		}
	}
	
	public static MancalaBoard initialBoard() {
		int[] pits = new int[NUM_LOCATIONS];
		for (Player player : Player.values()) {
			player.getPitLocations().forEach(pit -> pits[pit] = INITIAL_STONES_PER_PIT);
		}
		
		return new MancalaBoard(pits);
	}
	
	// Used for testing, for normal situations create a board using initialBoard()
	public MancalaBoard(int[] pits) {
		assert pits.length == NUM_LOCATIONS;
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
