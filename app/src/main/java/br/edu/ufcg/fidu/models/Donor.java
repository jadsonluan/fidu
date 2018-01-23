package br.edu.ufcg.fidu.models;

public class Donor extends User {

    public Donor(){

    }

    public Donor(String name, String email) {
        super(name, email);
    }

    public Donor(String name, String email, String occupation, String website) {
        super(name, email, occupation, website);
    }

}
