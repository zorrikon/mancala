package com.bol.mancala;

import java.util.Set;

public enum Player {
	BOTTOM("Bottom", 6, Set.of(0, 1, 2, 3, 4, 5)), 
	TOP("Top", 13, Set.of(7, 8, 9, 10, 11, 12));
	
	private final String name;
	private final int goalLocation;
	private final Set<Integer> pitLocations;
	
	private Player(String name, int goalLocation, Set<Integer> pitLocations) {
		this.name = name;
		this.goalLocation = goalLocation;
		this.pitLocations = pitLocations;
	}
	
	public Player otherPlayer() {
		return this == TOP ? BOTTOM : TOP;
	}
	
	public boolean canPlayInPitLocation(int pitLocation) {
		return this.pitLocations.contains(pitLocation);
	}
	
	public Set<Integer> getPitLocations() {
		return this.pitLocations;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getGoalLocation() {
		return this.goalLocation;
	}

	// TODO move to MancalaBoard?
	public static boolean isGoalPit(int pitLocation) {
		return pitLocation == BOTTOM.getGoalLocation() || pitLocation == TOP.getGoalLocation();
	}
}
