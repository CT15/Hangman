package com.thenewdomain.hangman;

/**
 * Created by CalvinTantio on 2/6/2017.
 */

public class UserInformation {
    final private static int NO_OF_CHARACTERS = 10;
    public String userName;
    public String currentChosenCharacter;
    public int rankPoints;

    public UserInformation(){
        this("");
    }

    public UserInformation(String userName){
        this.userName = userName;
        this.currentChosenCharacter = "character 1";
        this.rankPoints = 0;
    }

    //gameplay / features
    public void win(){
        this.rankPoints += 5;
    }

    public void lose(){
        this.rankPoints -= 5;
    }

    public void draw(){
        this.rankPoints += 1;
    }

    //getter methods
    public String getUserName() {
        return userName;
    }

    public String getCurrentChosenCharacter() {
        return currentChosenCharacter;
    }

    public int getRankPoints() {
        return rankPoints;
    }

    //setter methods used only when retrieving data from Firebase database
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRankPoints(int rankPoints) {
        this.rankPoints = rankPoints;
    }

    //other setter method
    public void setCurrentChosenCharacter(String currentChosenCharacter){
        this.currentChosenCharacter = currentChosenCharacter;
    }
}
