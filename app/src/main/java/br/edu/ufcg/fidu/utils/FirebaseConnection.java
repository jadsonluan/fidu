package br.edu.ufcg.fidu.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;

/**
 * Created by vitoria on 08/01/18.
 */

public class FirebaseConnection {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Context context;

    public FirebaseConnection(Context context){
        this.context = context;
    }

    private void saveDonee(String uid){
        DatabaseReference ref = database.getReference("users/donees/" + uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Donee donee = dataSnapshot.getValue(Donee.class);
                if(donee != null){
                    SaveData saveData = new SaveData(context);
                    saveData.writeDonatee(donee);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseConection", databaseError.getMessage());
            }
        });
    }


    private void saveDonor(String uid){
        DatabaseReference ref = database.getReference("users/donors/" + uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Donor donor = dataSnapshot.getValue(Donor.class);
                if(donor != null){
                    SaveData saveData = new SaveData(context);
                    saveData.writeDonator(donor);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseConection", databaseError.getMessage());
            }
        });
    }

    public void saveUser(String uid){
        saveDonee(uid);
        saveDonor(uid);
    }
}
