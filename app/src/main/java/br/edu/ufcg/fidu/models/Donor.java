package br.edu.ufcg.fidu.models;

public class Donor extends User {

    public Donor() {}

    public Donor(String uid, String name, String email, String occupation, String website,
                 String photoUrl, double lat, double lng) {
        super(uid, name, email, occupation, website, photoUrl, lat, lng);
    }

    public Donor(String uid, String name, String email) {
        this(uid, name, email, "", "", "", 0.0, 0.0);
    }
}
