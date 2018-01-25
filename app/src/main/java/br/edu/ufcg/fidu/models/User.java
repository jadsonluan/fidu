package br.edu.ufcg.fidu.models;

public class User {

    private String name;
    private String email;
    private String occupation;
    private String website;

    public User() {}

    public User(String name, String email) {
        this(name, email, "", "");
    }

    public User(String name, String email, String occupation, String website) {
        verifyParameters(name, email, occupation, website);
        this.name = name;
        this.email = email;
        this.occupation = occupation;
        this.website = website;
    }

    private void verifyParameters(String name, String email, String occupation, String website){
        if(name == null)
            throw new NullPointerException("Name cannot be null");

        if(email == null)
            throw new NullPointerException("Name cannot be null");

        if(occupation  == null)
            throw new NullPointerException("Name cannot be null");

        if (website == null)
            throw new NullPointerException("Website cannot be null");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() { return occupation; }

    public String getWebsite() { return website; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if (getClass() != o.getClass()) return false;

        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", occupation='" + occupation + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
