package br.edu.ufcg.fidu.models;

public class Donee extends User {

    private String address;

    public Donee(){
        super();

    }

    public Donee(String name, String email, String address) {
        super(name, email);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
