package br.edu.ufcg.fidu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;

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
        editor.putString("email_donor", donor.getEmail());
        editor.putString("name_donor", donor.getName());
        editor.putString("occupation_donor", donor.getOccupation());
        editor.putString("website_donor", donor.getWebsite());
        editor.apply();
    }

    public Donor readDonor(){
        String email = sharedPreferences.getString("email_donor", null);
        String name = sharedPreferences.getString("name_donor", null);
        String occupation = sharedPreferences.getString("occupation_donor", null);
        String website = sharedPreferences.getString("website_donor", null);
        return new Donor(name, email, occupation, website);
    }

    public void writeDonee(Donee donee){
        Log.d("SaveData", donee.toString());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("role", DONEE);
        editor.putBoolean("isLogged", true);
        editor.putString("email_donee", donee.getEmail());
        editor.putString("name_donee", donee.getName());
        editor.putString("address_donee", donee.getAddress());
        editor.putString("website_donee", donee.getWebsite());
        editor.putString("occupation_donee", donee.getOccupation());
        editor.putInt("benefited_donee", donee.getBenefited());
        editor.putInt("founded_in_donee", donee.getFoundedIn());
        editor.putString("description_donee", donee.getDescription());
        editor.apply();
    }

    public Donee readDonee(){
        String email = sharedPreferences.getString("email_donee", null);
        String name = sharedPreferences.getString("name_donee", null);
        String address = sharedPreferences.getString("address_donee", null);
        String website = sharedPreferences.getString("website_donee", null);
        String occupation = sharedPreferences.getString("occupation_donee", null);
        int benefited = sharedPreferences.getInt("benefited_donee", 0);
        int foundedIn = sharedPreferences.getInt("founded_in_donee", 0);
        String description = sharedPreferences.getString("description_donee", null);
        return new Donee(name, email, occupation, website, address, description, foundedIn, benefited);
    }

    public boolean isLogged(){
        return sharedPreferences.getBoolean("isLogged", false);
    }

    public int getRole() {
        if (!isLogged()) throw new IllegalStateException("User is not logged");
        return sharedPreferences.getInt("role", 0);
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("isLogged", false);
        editor.apply();
    }
}