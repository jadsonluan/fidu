package br.edu.ufcg.fidu.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.utils.DoneeAdapter;

public class SearchDoneeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DoneeAdapter adapter;
    private ListView doneeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donee);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        doneeList = findViewById(R.id.doneeList);
        populateListView();
    }

    private void populateListView() {
        Query query = mDatabase.child("users").child("donees");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Donee> donees = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Donee donee = data.getValue(Donee.class);
                    donees.add(donee);
                }

                fillAdapter(donees);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fillAdapter(ArrayList<Donee> donees) {
        adapter = new DoneeAdapter(donees, this);
        doneeList.setAdapter(adapter);
    }
}
