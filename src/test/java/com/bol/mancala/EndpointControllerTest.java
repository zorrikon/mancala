package com.bol.mancala;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EndpointController.class)
public class EndpointControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// get "/"
	
	@Test
	public void testInitialGetReturnsInitialBoard() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(model().attribute(EndpointController.PITS, MancalaBoard.initialBoard().getPits()))
			.andExpect(model().attribute(EndpointController.ACTIVE_PLAYER, Player.BOTTOM.getName()));
	}
	
	@Test
	public void testFillsInPitsAndPlayer() throws Exception {
		int[] pits = {2,0,0,0,1,0, 0, 3,0,0,5,0,0, 0};
		GameState gameState = new GameState(new MancalaBoard(pits), Player.TOP);
		
		this.mockMvc.perform(get("/").sessionAttr(EndpointController.GAME_STATE, gameState))
				.andExpect(view().name("app"))
				.andExpect(status().isOk())
				.andExpect(model().attribute(EndpointController.PITS, pits))
				.andExpect(model().attribute(EndpointController.ACTIVE_PLAYER, Player.TOP.getName()));
	}
	
	// move/*
	
	@Test
	public void testMove() throws Exception {
		int[] canCaptureBiggerPileByMovingPit1 = {0,2,0,0,1,0, 0, 3,0,5,0,0,0, 0};
		int[] expectedNextState                = {0,0,1,0,1,0, 6, 3,0,0,0,0,0, 0};
		GameState gameState = new GameState(new MancalaBoard(canCaptureBiggerPileByMovingPit1), 
											Player.BOTTOM);

		// TODO: Add expectations on the message and gameState
		this.mockMvc.perform(get("/move/1").sessionAttr(EndpointController.GAME_STATE, gameState))
				.andExpect(view().name("redirect:/"))
				.andExpect(status().is3xxRedirection());
		
		assertArrayEquals(expectedNextState, gameState.getBoard().getPits());
		assertEquals(Player.TOP, gameState.getActivePlayer());
	}	
	
	@Test
	public void testInvalidMovesDoNotChangeGameState() throws Exception {
		int[] pits = {0,2,0,0,1,0, 1, 3,0,5,0,0,0, 0};
		GameState gameState = new GameState(new MancalaBoard(Arrays.copyOf(pits, pits.length)), 
											Player.BOTTOM);
		
		{
			// Trying to move from an empty pit
			this.mockMvc.perform(get("/move/0").sessionAttr(EndpointController.GAME_STATE, gameState))
					.andExpect(view().name("redirect:/"))
					.andExpect(status().is3xxRedirection());
			
			assertArrayEquals(pits, gameState.getBoard().getPits());
			assertEquals(Player.BOTTOM, gameState.getActivePlayer());
		}
		
		{
			// Trying to move an opponent's pit
			this.mockMvc.perform(get("/move/7").sessionAttr(EndpointController.GAME_STATE, gameState))
					.andExpect(view().name("redirect:/"))
					.andExpect(status().is3xxRedirection());
			
			assertArrayEquals(pits, gameState.getBoard().getPits());
			assertEquals(Player.BOTTOM, gameState.getActivePlayer());
		}
		
		{
			// Trying to move a goal pit
			this.mockMvc.perform(get("/move/6").sessionAttr(EndpointController.GAME_STATE, gameState))
					.andExpect(view().name("redirect:/"))
					.andExpect(status().is3xxRedirection());
			
			assertArrayEquals(pits, gameState.getBoard().getPits());
			assertEquals(Player.BOTTOM, gameState.getActivePlayer());
		}
		
		{
			// Trying to move an invalid pit
			this.mockMvc.perform(get("/move/20").sessionAttr(EndpointController.GAME_STATE, gameState))
					.andExpect(view().name("redirect:/"))
					.andExpect(status().is3xxRedirection());
			
			assertArrayEquals(pits, gameState.getBoard().getPits());
			assertEquals(Player.BOTTOM, gameState.getActivePlayer());
		}
	}
	
	// bot-move
	
	@Test
	public void testBotMove() throws Exception {
		int[] canCaptureBiggerPileByMovingPit0 = {2,0,0,0,1,0, 0, 1,0,0,5,0,0, 0};
		GameState gameState = new GameState(new MancalaBoard(canCaptureBiggerPileByMovingPit0),
				                            Player.BOTTOM);
		
		this.mockMvc.perform(get("/bot-move").sessionAttr(EndpointController.GAME_STATE, gameState))
				.andExpect(view().name("redirect:/move/0"))
				.andExpect(status().is3xxRedirection());
	}
	
	// restart

	@Test
	public void testRestartReturnsInitialBoard() throws Exception {
		this.mockMvc.perform(get("/restart"))
			.andExpect(view().name("redirect:/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().size(0));
	}

	@Test
	public void testRestartReturnsInitialBoardEvenIfGameStateHasChanged() throws Exception {
		GameState gameState = new GameState(new MancalaBoard(new int[] {2,0,0,0,1,0, 0, 3,0,0,5,0,0, 0}),
				                            Player.TOP);
		
		this.mockMvc.perform(get("/restart").sessionAttr(EndpointController.GAME_STATE, gameState))
			.andExpect(view().name("redirect:/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().size(0));
	}
}
