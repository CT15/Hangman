package com.thenewdomain.hangman;

/**
 * Created by CalvinTantio on 10/7/2017.
 */

public class Message {
    String message0;

    public Message(){

    }

    public Message(String message){
        this.message0 = message;
    }

    public String getMessage() {
        return message0;
    }

    public void setMessage(String message) {
        this.message0 = message;
    }
}
