package com.bol.mancala;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bol.mancala.bot.MancalaBot;

@Controller
public class EndpointController {
	
	// TODO Possibly consolidate gameState with pits and activePlayer.
	static final String GAME_STATE = "gameState";
	static final String PITS = "pits";
	static final String ACTIVE_PLAYER = "activePlayer";
	
	static final String MESSAGE = "message";
	
	@GetMapping("/")
	public static String fillInModel(HttpServletRequest request, Map<String, Object> model) {
		HttpSession session = request.getSession();
		GameState gameState = getGameState(session);
		
		if (gameState.isGameOver()) {
			session.invalidate();
			model.put(MESSAGE, gameState.getGameResultMessage());
		}
	
		model.put(PITS, gameState.getBoard().getPits());
		model.put(ACTIVE_PLAYER, gameState.getActivePlayer().getName());
		return "app";
	}
	
	@GetMapping("/move/{pitLocation}")
	public static String move(HttpServletRequest request, @PathVariable String pitLocation) {
		HttpSession session = request.getSession();
		GameState gameState = getGameState(session);
		String message = gameState.doMove(Integer.parseInt(pitLocation));
		session.setAttribute(GAME_STATE, gameState);
		session.setAttribute(MESSAGE, message);
		return "redirect:/";
	}
	
	@GetMapping("/bot-move")
	public static String makeBotMove(HttpServletRequest request) {
		GameState gameState = getGameState(request.getSession());
		int bestMove = MancalaBot.bestMove(gameState, 5 /* depth */); // Much more depth will take too long
		return "redirect:/move/" + String.valueOf(bestMove);
	}
	
	@GetMapping("/restart")
	public static String restartGame(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	}
	
	private static GameState getGameState(HttpSession session) {
		return (GameState) Optional.ofNullable(session.getAttribute(GAME_STATE))
				.orElse(GameState.initialState());
	}
}
