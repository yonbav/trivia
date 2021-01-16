package com.adaptionsoft.games.trivia;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.adaptionsoft.games.uglytrivia.Game;
import org.junit.Test;

public class GameTest {
	@Test
	public void isPlayableMoreThenTwoPlayers() {
		final var game = createNewGame(3);
		var result = game.isPlayable();
		assertTrue("When there are more than 2 players isPlayable should return true", result);
	}

	@Test
	public void isPlayableLessThenTwoPlayers() {
		final var game = createNewGame(1);
		var result = game.isPlayable();
		assertFalse("When there are less than 2 players isPlayable should return false", result);
	}

	@Test
	public void isPlayableTwoPlayers() {
		final var game = createNewGame(2);
		var result = game.isPlayable();
		assertTrue("When there are exactly 2 players isPlayable should return true", result);
	}

	@Test
	public void isPlayableSixPlayers() {
		final var game = createNewGame(6);
		var result = game.isPlayable();
		assertTrue("When there are exactly 6 players isPlayable should return true", result);
	}

	@Test
	public void isPlayableMoreThanSixPlayers() {
		final var game = createNewGame(7);
		var result = game.isPlayable();
		assertFalse("When there are more than 6 players isPlayable should return false", result);
	}

	private Game createNewGame(int playersNumber) {
		final var game = new Game();
		for (int i = 0; i < playersNumber; i++) {
			game.add("test-" + i);
		}
		return game;
	}
}
