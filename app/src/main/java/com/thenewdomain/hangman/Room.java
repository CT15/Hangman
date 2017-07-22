package com.thenewdomain.hangman;

import java.util.ArrayList;

/**
 * Created by CalvinTantio on 28/6/2017.
 */

public class Room {
    public String roomName;
    public String player1ID;
    public String player2ID;
    public String gameState;
    public String secretWord1;
    public String secretWord2;
    public int life1;
    public int life2;
    public int gameStanding;
    public ArrayList<String> messages1;
    public ArrayList<String> messages2;
    public String blankSpace1;
    public String blankSpace2;
    public boolean player1IsOut;
    public boolean player2IsOut;
    public ArrayList<String> chosen1;
    public ArrayList<String> chosen2;

    public Room(){

    }

    public Room(String roomName) {
        this.roomName = roomName;
        this.player1ID = "";
        this.player2ID = "";
        this.gameState = "player_1_setting_word";
        this.secretWord1 = "";
        this.secretWord2 = "";
        this.life1 = 7;
        this.life2 = 7;
        this.gameStanding = 0;
        this.messages1 = new ArrayList<String>();
        this.messages1.add("Battle Start!");
        this.messages2 = new ArrayList<String>();
        this.messages2.add("Battle Start!");
        this.blankSpace1 = "";
        this.blankSpace2 = "";
        this.player1IsOut = false;
        this.player2IsOut = false;
        this.chosen1 = new ArrayList<String>();
        this.chosen1.add("test");
        this.chosen2 = new ArrayList<String>();
        this.chosen2.add("test");
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPlayer1ID() {
        return player1ID;
    }

    public void setPlayer1ID(String player1ID) {
        this.player1ID = player1ID;
    }

    public String getPlayer2ID() {
        return player2ID;
    }

    public void setPlayer2ID(String player2ID) {
        this.player2ID = player2ID;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getSecretWord1() {
        return secretWord1;
    }

    public void setSecretWord1(String secretWord1) {
        this.secretWord1 = secretWord1;
    }

    public String getSecretWord2() {
        return secretWord2;
    }

    public void setSecretWord2(String secretWord2) {
        this.secretWord2 = secretWord2;
    }

    public int getGameStanding() {
        return gameStanding;
    }

    public void setGameStanding(int gameStanding) {
        this.gameStanding = gameStanding;
    }

    public int getLife1() {
        return life1;
    }

    public void setLife1(int life1) {
        this.life1 = life1;
    }

    public int getLife2() {
        return life2;
    }

    public void setLife2(int life2) {
        this.life2 = life2;
    }

    public ArrayList<String> getMessages1() {
        return messages1;
    }

    public void setMessages1(ArrayList<String> messages1) {
        this.messages1 = messages1;
    }

    public ArrayList<String> getMessages2() {
        return messages2;
    }

    public void setMessages2(ArrayList<String> messages2) {
        this.messages2 = messages2;
    }

    public String getBlankSpace1() {
        return blankSpace1;
    }

    public void setBlankSpace1(String blankSpace1) {
        this.blankSpace1 = blankSpace1;
    }

    public String getBlankSpace2() {
        return blankSpace2;
    }

    public void setBlankSpace2(String blankSpace2) {
        this.blankSpace2 = blankSpace2;
    }

    public boolean isPlayer1IsOut() {
        return player1IsOut;
    }

    public void setPlayer1IsOut(boolean player1IsOut) {
        this.player1IsOut = player1IsOut;
    }

    public boolean isPlayer2IsOut() {
        return player2IsOut;
    }

    public void setPlayer2IsOut(boolean player2IsOut) {
        this.player2IsOut = player2IsOut;
    }

    public ArrayList<String> getChosen1() {
        return chosen1;
    }

    public void setChosen1(ArrayList<String> chosen1) {
        this.chosen1 = chosen1;
    }

    public ArrayList<String> getChosen2() {
        return chosen2;
    }

    public void setChosen2(ArrayList<String> chosen2) {
        this.chosen2 = chosen2;
    }
}
