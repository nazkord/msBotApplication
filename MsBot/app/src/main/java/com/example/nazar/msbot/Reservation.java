package com.example.nazar.msbot;

import java.io.Serializable;

public class Reservation implements Serializable {

    public Reservation() {
    }

    public Reservation(String userName, String userPassword, Integer timeOfGame, String halfOfField) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.timeOfGame = timeOfGame;
        this.halfOfField = halfOfField;
    }

    private String userName;
    private String userPassword;
    private Integer timeOfGame;
    private String halfOfField;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getTimeOfGame() {
        return timeOfGame;
    }

    public void setTimeOfGame(Integer timeOfGame) {
        this.timeOfGame = timeOfGame;
    }

    public String getHalfOfField() {
        return halfOfField;
    }

    public void setHalfOfField(String halfOfField) {
        this.halfOfField = halfOfField;
    }

}
