package br.edu.ufcg.fidu.models;

public class Donor extends User {

    public Donor() {}

    public Donor(String uid, String name, String email) {
        super(uid, name, email);
    }

    public Donor(String uid, String name, String email, String occupation, String website) {
        super(uid, name, email, occupation, website, "");
    }

    public Donor(String uid, String name, String email, String occupation, String website, String photoUrl) {
        super(uid, name, email, occupation, website, photoUrl);
    }
}
