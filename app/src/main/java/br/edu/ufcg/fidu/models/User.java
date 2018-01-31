package br.edu.ufcg.fidu.models;

public class User {

    private String uid;
    private String name;
    private String email;
    private String occupation;
    private String website;
    private String photoUrl;
    private double lat;
    private double lng;

    public User() {}

    public User(String uid, String name, String email) {
        this(uid, name, email, "", "", "", 0.0, 0.0);
    }

    public User(String uid, String name, String email, String occupation, String website,
                String photoUrl, double lat, double lng) {
        verifyParameters(uid, name, email, occupation, website, photoUrl);
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.occupation = occupation;
        this.website = website;
        this.photoUrl = photoUrl;
        this.lat = lat;
        this.lng = lng;
    }

    private void verifyParameters(String uid, String name, String email, String occupation, String website,
                                  String photoUrl){
        if (uid == null)
            throw new NullPointerException("Uid cannot be null");

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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String getUid() { return uid; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() { return occupation; }

    public String getWebsite() { return website; }

    public String getPhotoUrl() { return photoUrl; }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

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