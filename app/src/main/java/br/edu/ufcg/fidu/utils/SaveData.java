package br.edu.ufcg.fidu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;
import br.edu.ufcg.fidu.models.User;

public class SaveData {
    public static final int DONOR = 0;
    public static final int DONEE = 1;

    private static final String PREFERENCES_KEY = "br.edu.ufcg.fidu.PREFERENCE_FILE_KEY";
    private final SharedPreferences sharedPreferences;

    public SaveData(Context context){
        sharedPreferences = context.getSharedPreferences(
                PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public void writeDonor(Donor donor){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("role", DONOR);
        editor.putBoolean("isLogged", true);
        editor.putString("uid", donor.getUid());
        editor.putString("email_donor", donor.getEmail());
        editor.putString("photo_donor", donor.getPhotoUrl());
        editor.putString("name_donor", donor.getName());
        editor.putString("occupation_donor", donor.getOccupation());
        editor.putString("website_donor", donor.getWebsite());
        editor.putFloat("lat_user", (float) donor.getLat());
        editor.putFloat("lat_user", (float) donor.getLat());
        editor.apply();
    }

    public Donor readDonor(){
        String uid = sharedPreferences.getString("uid", null);
        String email = sharedPreferences.getString("email_donor", null);
        String photoUrl = sharedPreferences.getString("photo_donor", null);
        String name = sharedPreferences.getString("name_donor", null);
        String occupation = sharedPreferences.getString("occupation_donor", null);
        String website = sharedPreferences.getString("website_donor", null);
        double lat = sharedPreferences.getFloat("lat_user", 0);
        double lng = sharedPreferences.getFloat("lng_user", 0);
        return new Donor(uid, name, email, occupation, website, photoUrl, lat, lng);
    }

    public void writeDonee(Donee donee){
        Log.d("SaveData", donee.toString());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("role", DONEE);
        editor.putBoolean("isLogged", true);
        editor.putString("uid", donee.getUid());
        editor.putString("email_donee", donee.getEmail());
        editor.putString("photo_donee", donee.getPhotoUrl());
        editor.putString("name_donee", donee.getName());
        editor.putString("address_donee", donee.getAddress());
        editor.putString("website_donee", donee.getWebsite());
        editor.putString("occupation_donee", donee.getOccupation());
        editor.putInt("benefited_donee", donee.getBenefited());
        editor.putInt("founded_in_donee", donee.getFoundedIn());
        editor.putString("description_donee", donee.getDescription());
        editor.putFloat("lat_user", (float) donee.getLat());
        editor.putFloat("lat_user", (float) donee.getLat());
        editor.apply();
    }

    public Donee readDonee(){
        String uid = sharedPreferences.getString("uid", null);
        String email = sharedPreferences.getString("email_donee", null);
        String photoUrl = sharedPreferences.getString("photo_donee", null);
        String name = sharedPreferences.getString("name_donee", null);
        String address = sharedPreferences.getString("address_donee", null);
        String website = sharedPreferences.getString("website_donee", null);
        String occupation = sharedPreferences.getString("occupation_donee", null);
        int benefited = sharedPreferences.getInt("benefited_donee", 0);
        int foundedIn = sharedPreferences.getInt("founded_in_donee", 0);
        String description = sharedPreferences.getString("description_donee", null);
        double lat = sharedPreferences.getFloat("lat_user", 0);
        double lng = sharedPreferences.getFloat("lng_user", 0);
        return new Donee(uid, name, email, occupation, website, photoUrl, lat, lng, address,
                description, foundedIn, benefited);
    }

    public boolean isLogged(){
        return sharedPreferences.getBoolean("isLogged", false);
    }

    public int getRole() {
        if (!isLogged()) throw new IllegalStateException("User is not logged");
        return sharedPreferences.getInt("role", 0);
    }

    public User getUser() {
        if (!isLogged()) throw new IllegalStateException("User is not logged");

        if (getRole() == DONEE) return readDonee();
        else return readDonor();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("isLogged", false);
        editor.apply();
    }
}