package br.edu.ufcg.fidu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;

/**
 * Created by vitoria on 08/01/18.
 */

public class SaveData {
    private static final String PREFERENCES_KEY = "br.edu.ufcg.fidu.PREFERENCE_FILE_KEY";
    private SharedPreferences sharedPreferences;

    public SaveData(Context context){
        sharedPreferences = context.getSharedPreferences(
                PREFERENCES_KEY, context.MODE_PRIVATE);
    }

    public void writeDonator(Donor donor){
        SharedPreferences.Editor editor = sharedPreferences.edit();
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

    public void writeDonatee(Donee donee){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogged", true);
        editor.putString("email_donee", donee.getEmail());
        editor.putString("name_donee", donee.getName());
        editor.putString("address_donee", donee.getAddress());
        editor.commit();
    }

    public Donee readDonatee(){
        String email = sharedPreferences.getString("email_donee", null);
        String name = sharedPreferences.getString("name_donee", null);
        String address = sharedPreferences.getString("address_donee", null);
        return new Donee(name, email, address);
    }

    public boolean isLogged(){
        return sharedPreferences.getBoolean("isLogged", false);
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("isLogged", false);
        editor.commit();
    }
}