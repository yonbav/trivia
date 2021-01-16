
package com.adaptionsoft.games.trivia.runner;

import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;


public class GameRunner {

    public static void main(String[] args) throws Exception {
        Game aGame = new Game();
        addPlayers(aGame);
        if (!aGame.isPlayable()) {
            throw new Exception("Not enough players. game has " + aGame.getPlayersNumber() + " player");
        }
        Random rand = new Random();
        boolean noWinner;
        do {
            noWinner = playRound(aGame, rand);
        } while (noWinner);
    }

    private static boolean playRound(Game aGame, Random rand) {
        aGame.roll(rand.nextInt(5) + 1);
        if (rand.nextInt(9) == 7) {
            return aGame.wrongAnswer();
        } else {
            return aGame.wasCorrectlyAnswered();
        }
    }

    private static void addPlayers(Game aGame) {
        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");
    }
}
