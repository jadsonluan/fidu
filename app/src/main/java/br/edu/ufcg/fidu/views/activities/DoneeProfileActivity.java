package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;

public class DoneeProfileActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvOccupation;
    private TextView tvDescription;
    private TextView tvFoundedIn;
    private TextView tvBenefited;
    private TextView tvAddress;
    private TextView tvWebsite;
    private ImageView backdrop;
    private ProgressBar loading;

    private FloatingActionButton btnContact;

    private ViewGroup occupationLayout;
    private ViewGroup descriptionLayout;
    private ViewGroup foundedInLayout;
    private ViewGroup benefitedLayout;
    private ViewGroup addressLayout;
    private ViewGroup websiteLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donee_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loading = findViewById(R.id.main_loading);
        backdrop = findViewById(R.id.main_backdrop);
        tvName = findViewById(R.id.tvName);
        tvOccupation = findViewById(R.id.tvOccupation);
        tvDescription = findViewById(R.id.tvDescription);
        tvFoundedIn = findViewById(R.id.tvFoundedIn);
        tvBenefited = findViewById(R.id.tvBenefited);
        tvWebsite = findViewById(R.id.tvWebsite);
        tvAddress = findViewById(R.id.tvAddress);

        occupationLayout = findViewById(R.id.occupationLayout);
        descriptionLayout = findViewById(R.id.descriptionLayout);
        foundedInLayout = findViewById(R.id.foundedInLayout);
        benefitedLayout = findViewById(R.id.benefitedLayout);
        websiteLayout = findViewById(R.id.websiteLayout);
        addressLayout = findViewById(R.id.addressLayout);

        btnContact = findViewById(R.id.btnContact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoneeProfileActivity.this, "Contatando instituição", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            onBackPressed();
        } else if (!extras.containsKey("donee_uid")) {
            onBackPressed();
        }

        updateUI();
    }

    private void updateUI() {
        // Loading starts
        String uid = getIntent().getExtras().getString("donee_uid");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("users").child("donees").child(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Donee donee = dataSnapshot.getValue(Donee.class);
                if (donee != null) {
                    tvName.setText(donee.getName());
                    setInformation(donee.getOccupation(), tvOccupation, occupationLayout);
                    setInformation(donee.getWebsite(), tvWebsite, websiteLayout);
                    loadPhoto(donee.getPhotoUrl());

                    String benefited, foundedIn;
                    benefited = donee.getBenefited() == 0 ? "" : donee.getBenefited() + " pessoa(s)";
                    foundedIn = donee.getFoundedIn() == 0 ? "" : donee.getFoundedIn() + "";

                    setInformation(donee.getAddress(), tvAddress, addressLayout);
                    setInformation(donee.getDescription(), tvDescription, descriptionLayout);
                    setInformation(benefited, tvBenefited, benefitedLayout);
                    setInformation(foundedIn, tvFoundedIn, foundedInLayout);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                startActivity(new Intent(DoneeProfileActivity.this, SearchDoneeActivity.class));
                finish();
            }
        });
    }


    /**
     * Adiciona informação ao TextView especificado. Caso a informação seja vazia,
     * o layout que possui o TextView ficará oculto.
     *
     * @param value a informação a ser atribuída
     * @param view o componente que vai exibir a informação
     * @param layout o layout onde o <b>view</b> está contido
     */
    private void setInformation(String value, TextView view, ViewGroup layout) {
        if (value.equals("")) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
            view.setText(value);
        }
    }

    /**
     * Carrega a imagem de URL especificada na view da foto de perfil do usuário
     *
     * @param url URL da imagem a ser carregada
     */
    private void loadPhoto(String url) {
        if (!url.equals("")) {
            showProgress(true);
            Glide.with(backdrop.getContext())
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            showProgress(false);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            showProgress(false);
                            return false;
                        }
                    })
                    .into(backdrop);
        }
    }

    private void showProgress(boolean show) {
        loading.setVisibility(show ? View.VISIBLE : View.GONE);
        backdrop.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
