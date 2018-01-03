package br.edu.ufcg.fidu.models;

public class Donatory extends User {
    private String address;

    public Donatory(String name, String email, String password, String address) {
        super(name, email, password);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
