package br.edu.ufcg.fidu.models;

/**
 * Created by vitoria on 28/01/18.
 */

public class Message {
    private String user;
    private String message;
    private String date;

    public Message(){

    }

    public Message(String user, String message, String date){
        this.user = user;
        this.message = message;
    }

    public String getUser(){
        return user;
    }

    public String getMessage(){
        return message;
    }

    public String getDate(){
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date){
        this.date = date;
    }
}
