package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Game {
    private static final int QUESTION_BATCH_SIZE = 50;
    private static final int BOARD_SIZE = QuestionCategory.values().length * 3;
    private final ArrayList<Player> players = new ArrayList<>();
    private final Map<QuestionCategory, QuestionsDeck> categorizedQuestions = new HashMap<>();

    private int currentPlayerIndex = 0;
    private int questionsIndex = 1;

    public Game() {
        createQuestionsDeck();
        addQuestions();
        System.out.println("Board size: " + BOARD_SIZE);
    }

    private void createQuestionsDeck() {
        for (QuestionCategory category : QuestionCategory.values()) {
            categorizedQuestions.put(category, new QuestionsDeck(new LinkedList<>()));
        }
    }

    private void addQuestions() {
        for (int i = questionsIndex; i < questionsIndex + QUESTION_BATCH_SIZE; i++) {
            for (QuestionCategory category : QuestionCategory.values()) {
                categorizedQuestions.get(category).getQuestions().add(category.getDisplayName() + " Question " + i);
            }
        }
        questionsIndex += QUESTION_BATCH_SIZE;
    }

    public boolean isPlayable() {
        final var playersNumber = getPlayersNumber();
        return (playersNumber >= 2 && playersNumber <= 6);
    }

    public boolean add(String playerName) {
        final var newPlayer = new Player(playerName, 0, 0, false);
        players.add(newPlayer);
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public void roll(int roll) {
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        System.out.println(currentPlayer.getName() + " is the current player");
        System.out.println("They have rolled a " + roll);
        handlePenalty(roll);
        if (!currentPlayer.getInPenaltyBox()) {
            runRoll(roll);
        }
    }

    private void handlePenalty(int roll) {
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        if (!currentPlayer.getInPenaltyBox()) {
            return;
        }
        if (roll % 2 != 0) {
            currentPlayer.setInPenaltyBox(false);
            System.out.println(currentPlayer.getName() + " is getting out of the penalty box");
            runRoll(roll);
        } else {
            System.out.println(currentPlayer.getName() + " is not getting out of the penalty box");
        }
    }

    private void runRoll(int roll) {
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        currentPlayer.setPlace(currentPlayer.getPlace() + roll);
        if (currentPlayer.getPlace() >= BOARD_SIZE) {
            currentPlayer.setPlace(currentPlayer.getPlace() - BOARD_SIZE);
        }
        System.out.println(currentPlayer.getName()
                + "'s new location is "
                + currentPlayer.getPlace());
        System.out.println("The category is " + currentCategory().getDisplayName());
        askQuestion();
    }

    private void askQuestion() {
        final var category = currentCategory();
        final var question = categorizedQuestions.get(category).getQuestions().removeFirst();
        System.out.println(question);
        refillQuestionsIfNeeded();
    }

    private void refillQuestionsIfNeeded() {
        final var shouldAddQuestions = Arrays.stream(QuestionCategory.values())
                .map(category -> categorizedQuestions.get(category).getQuestions().isEmpty())
                .reduce((acc, cur) -> acc && cur);
        if (shouldAddQuestions.isPresent() && shouldAddQuestions.get()) {
            addQuestions();
        }
    }


    private QuestionCategory currentCategory() {
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        final var categoryIndex = currentPlayer.getPlace() % QuestionCategory.values().length;
        return QuestionCategory.values()[categoryIndex];
    }

    public boolean wasCorrectlyAnswered() {
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        if (!currentPlayer.getInPenaltyBox()) {
            handleCorrectAnswer();
        }
        return getNextPlayer();
    }

    private void handleCorrectAnswer() {
        System.out.println("Answer was correct!!!!");
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        currentPlayer.setPurse(currentPlayer.getPurse() + 1);
        System.out.println(players.get(currentPlayerIndex)
                + " now has "
                + currentPlayer.getPurse()
                + " Gold Coins.");
    }

    private boolean getNextPlayer() {
        final var didWin = didPlayerWin();
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) {
            currentPlayerIndex = 0;
        }
        return didWin;
    }

    public boolean wrongAnswer() {
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        currentPlayer.setInPenaltyBox(true);
        System.out.println("Question was incorrectly answered");
        System.out.println(players.get(currentPlayerIndex) + " was sent to the penalty box");
        return getNextPlayer();
    }


    private boolean didPlayerWin() {
        final var currentPlayer = getCurrentPlayer(currentPlayerIndex);
        return currentPlayer.getPurse() != 6;
    }

    private Player getCurrentPlayer(int index) {
        return players.get(index);
    }
}
