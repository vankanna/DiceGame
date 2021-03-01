package edu.wctc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiceGame {
    final private List<Player> players = new ArrayList<Player>();
    final private List<Die> dice = new ArrayList<Die>();
    final private int maxRolls;
    private Player currentPlayer;

    public DiceGame(int countPlayers, int countDice, int maxRolls) {

        if(countPlayers < 2) {
            throw new IllegalArgumentException("Must be greater than 2");
        }
        IntStream.range(0, countPlayers)
                .forEach(x -> players.add(new Player()));

        IntStream.range(0, countDice)
                .forEach(x -> dice.add(new Die(6)));

        this.maxRolls = maxRolls;
    }

    private boolean allDiceHeld() {
        return dice.stream().allMatch(Die::isBeingHeld);
    }
    public boolean autoHold( int faceValue ) {
        Optional<Die> first = dice.stream().filter((x) -> x.getFaceValue() == faceValue).findFirst();
        if (first.isPresent()) {
            Die die = first.get();
            if (!die.isBeingHeld()) {
                die.holdDie();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean currentPlayerCanRoll() {
        boolean response = this.currentPlayer.getRollsUsed() <= maxRolls && !allDiceHeld();
        return response;
    }

    public int getCurrentPlayerNumber() {
        return this.currentPlayer.getPlayerNumber();
    }

    public int getCurrentPlayerScore() {
        return this.currentPlayer.getScore();
    }

    public String getDiceResults() {
        String output = this.dice.stream().map(Die::toString).collect(Collectors.joining());
        return output;
    }

    public String getFinalWinner() {
        Player player = Collections.max(players, Comparator.comparingInt(Player::getWins));
        return player.toString();
    }

    public String getGameResults() {
        List<Player> sortedList = players.stream()
                .sorted(Comparator.comparingInt(Player::getScore).reversed())
                .collect(Collectors.toList());
        sortedList.forEach(p -> {

        });
        return sortedList.stream().map(Player::toString).collect(Collectors.joining());
    }

    private boolean isHoldingDie(int faceValue) {
        Optional<Die> first = dice.stream()
                .filter(Die::isBeingHeld)
                .filter( x-> x.getFaceValue() == faceValue)
                .findFirst();

        return first.isPresent();
    }

    public boolean nextPlayer() {
        if (this.currentPlayer.getPlayerNumber() < players.size()) {
            this.currentPlayer = players.get(this.currentPlayer.getPlayerNumber());
            return true;
        } else {
            return false;
        }
    }

    public void playerHold(char dieNumber) {
        Optional<Die> first = dice.stream()
                .filter(x -> x.getDieNum() == dieNumber)
                .findFirst();
        if(first.isPresent()) {
            Die die = first.get();
            die.holdDie();
        }
    }

    public void resetDice() {
        dice.forEach(Die::resetDie);
    }

    public void resetPlayers() {
        players.forEach(Player::resetPlayer);
    }

    public void rollDice() {
        dice.forEach(Die::rollDie);
        this.currentPlayer.roll();
    }

    public void  scoreCurrentPlayer() {
        if (isHoldingDie(6) && isHoldingDie(5) && isHoldingDie(4)) {
            this.currentPlayer.setScore( dice.stream().mapToInt(Die::getFaceValue).sum() - 15);
        }
    }

    public void startNewGame() {
        this.currentPlayer = players.get(0);
        resetPlayers();
    }

}
