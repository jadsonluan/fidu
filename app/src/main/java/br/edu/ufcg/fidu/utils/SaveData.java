package br.edu.ufcg.fidu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;

/**
 * Created by vitoria on 08/01/18.
 */

public class SaveData {
    public static final int DONOR = 0;
    public static final int DONEE = 1;

    private static final String PREFERENCES_KEY = "br.edu.ufcg.fidu.PREFERENCE_FILE_KEY";
    private SharedPreferences sharedPreferences;

    public SaveData(Context context){
        sharedPreferences = context.getSharedPreferences(
                PREFERENCES_KEY, context.MODE_PRIVATE);
    }

    public void writeDonor(Donor donor){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("role", DONOR);
        editor.putBoolean("isLogged", true);
        editor.putString("email_donor", donor.getEmail());
        editor.putString("name_donor", donor.getName());
        editor.commit();
    }

    public Donor readDonor(){
        String email = sharedPreferences.getString("email_donor", null);
        String name = sharedPreferences.getString("name_donor", null);
        return new Donor(name, email);
    }

    public void writeDonee(Donee donee){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("role", DONEE);
        editor.putBoolean("isLogged", true);
        editor.putString("email_donee", donee.getEmail());
        editor.putString("name_donee", donee.getName());
        editor.putString("address_donee", donee.getAddress());
        editor.commit();
    }

    public Donee readDonee(){
        String email = sharedPreferences.getString("email_donee", null);
        String name = sharedPreferences.getString("name_donee", null);
        String address = sharedPreferences.getString("address_donee", null);
        return new Donee(name, email, address);
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
        editor.commit();
    }
}