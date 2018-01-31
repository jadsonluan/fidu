package br.edu.ufcg.fidu.models;

public class Donee extends User {

    private String address;
    private String description;
    private int foundedIn;
    private int benefited;

    public Donee() {}

    public Donee(String uid, String name, String email, String occupation, String website, String photoUrl,
                 double lat, double lng, String address, String description, int foundedIn, int benefited) {
        super(uid, name, email, occupation, website, photoUrl, lat, lng);
        verifyParams(address, description, foundedIn, benefited);
        this.address = address;
        this.description = description;
        this.foundedIn = foundedIn;
        this.benefited = benefited;
    }

    public Donee(String uid, String name, String email, String address) {
        this(uid, name, email, "", "", "", 0.0, 0.0, address,
                "", 0, 0);
    }

    private void verifyParams(String address, String description, int foundedIn, int benefited) {
        if (address == null) throw new NullPointerException("Address cannot be null");
        if (description == null) throw new NullPointerException("Description cannot be null");
        if (foundedIn < 0) throw new NullPointerException("FoundedIn cannot be negative");
        if (benefited < 0) throw new IllegalArgumentException("Benefited cannot be negative");
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public int getFoundedIn() {
        return foundedIn;
    }

    public int getBenefited() {
        return benefited;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFoundedIn(int foundedIn) {
        this.foundedIn = foundedIn;
    }

    public void setBenefited(int benefited) {
        this.benefited = benefited;
    }

    @Override
    public String toString() {
        return "Donee{" +
                "address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", foundedIn='" + foundedIn + '\'' +
                ", benefited='" + benefited + '\'' +
                "} " + super.toString();
    }
}
