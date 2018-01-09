package br.edu.ufcg.fidu.models;

public class User {

    private String name;
    private String email;

    public User(){

    }

    public User(String name, String email) {
        verifyParameters(name, email);
        this.name = name;
        this.email = email;
    }

    private void verifyParameters(String name, String email){
        if(name == null || email == null)
            throw new NullPointerException("Name or email cannot be null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}
