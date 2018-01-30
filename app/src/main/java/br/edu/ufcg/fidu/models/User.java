package br.edu.ufcg.fidu.models;

public class User {

    private String name;
    private String email;
    private String occupation;
    private String website;
    private String photoUrl;

    public User() {}

    public User(String name, String email) {
        this(name, email, "", "", "");
    }

    public User(String name, String email, String occupation, String website) {
        this(name, email, occupation, website, "");
    }

    public User(String name, String email, String occupation, String website, String photoUrl) {
        verifyParameters(name, email, occupation, website, photoUrl);
        this.name = name;
        this.email = email;
        this.occupation = occupation;
        this.website = website;
        this.photoUrl = photoUrl;
    }

    private void verifyParameters(String name, String email, String occupation, String website,
                                  String photoUrl){
        if(name == null)
            throw new NullPointerException("Name cannot be null");

        if(email == null)
            throw new NullPointerException("Email cannot be null");

        if(occupation  == null)
            throw new NullPointerException("Occupation cannot be null");

        if (website == null)
            throw new NullPointerException("Website cannot be null");

        if (photoUrl == null)
            throw new NullPointerException("PhotoUrl cannot me null");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() { return occupation; }

    public String getWebsite() { return website; }

    public String getPhotoUrl() { return photoUrl; }

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
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
