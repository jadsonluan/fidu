package br.edu.ufcg.fidu.models;

public class Donee extends User {

    private final String address;
    private final String description;
    private final int foundedIn;
    private final int benefited;

    public Donee(String name, String email, String address) {
        this(name, email, "", "", address, "", 0, 0);
    }

    public Donee(String name, String email, String occupation, String website, String address,
                 String description, int foundedIn, int benefited) {
        super(name, email, occupation, website);
        verifyParams(address, description, foundedIn, benefited);
        this.address = address;
        this.description = description;
        this.foundedIn = foundedIn;
        this.benefited = benefited;
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
