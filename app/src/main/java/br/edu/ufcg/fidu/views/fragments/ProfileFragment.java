package br.edu.ufcg.fidu.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.activities.InitialActivity;
import br.edu.ufcg.fidu.views.activities.UpdateProfileActivity;

public class ProfileFragment extends Fragment {
    private TextView tvName;
    private TextView tvOccupation;
    private TextView tvDescription;
    private TextView tvFoundedIn;
    private TextView tvBenefited;
    private TextView tvAddress;
    private TextView tvWebsite;
    private ImageView backdrop;

    private ViewGroup occupationLayout;
    private ViewGroup descriptionLayout;
    private ViewGroup foundedInLayout;
    private ViewGroup benefitedLayout;
    private ViewGroup addressLayout;
    private ViewGroup websiteLayout;

    private StorageReference mStorage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mStorage = FirebaseStorage.getInstance().getReference();

        // corrige problema com o titulo da appbar
        final CollapsingToolbarLayout collapsingToolbarLayout;
        collapsingToolbarLayout = view.findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = view.findViewById(R.id.main_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.title_profile));
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        backdrop = view.findViewById(R.id.main_backdrop);
        tvName = view.findViewById(R.id.tvName);
        tvOccupation = view.findViewById(R.id.tvOccupation);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvFoundedIn = view.findViewById(R.id.tvFoundedIn);
        tvBenefited = view.findViewById(R.id.tvBenefited);
        tvWebsite = view.findViewById(R.id.tvWebsite);
        tvAddress = view.findViewById(R.id.tvAddress);

        occupationLayout = view.findViewById(R.id.occupationLayout);
        descriptionLayout = view.findViewById(R.id.descriptionLayout);
        foundedInLayout = view.findViewById(R.id.foundedInLayout);
        benefitedLayout = view.findViewById(R.id.benefitedLayout);
        websiteLayout = view.findViewById(R.id.websiteLayout);
        addressLayout = view.findViewById(R.id.addressLayout);

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        FloatingActionButton btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateProfile() {
        startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void updateUI() {
        SaveData sv = new SaveData(getActivity());

        if (sv.isLogged()) {
            String unknown = getString(R.string.unknown_information);
            User user;
            int role = sv.getRole();

            if (role == SaveData.DONEE) {
                user = sv.readDonee();
            } else if (role == SaveData.DONOR) {
                user = sv.readDonor();
            } else {
                return;
            }

            String occupation = user.getOccupation().equals("") ? unknown : user.getOccupation();
            String website = user.getWebsite().equals("") ? unknown : user.getWebsite();

            tvName.setText(user.getName());
            tvOccupation.setText(occupation);
            tvWebsite.setText(website);

            updatePhoto();

            if (user instanceof Donee) {
                // Passa dados do Donee
                Donee donee = (Donee) user;
                String benefited, foundedIn, description;

                benefited = donee.getBenefited() == 0 ? unknown : donee.getBenefited() + " pessoa(s)";
                foundedIn = donee.getFoundedIn() == 0 ? unknown : donee.getFoundedIn() + "";
                description = donee.getDescription().equals("") ? unknown : donee.getDescription();

                tvAddress.setText(donee.getAddress());
                tvDescription.setText(description);
                tvBenefited.setText(benefited);
                tvFoundedIn.setText(foundedIn);
            } else {
                // Oculta informações do Donee
                addressLayout.setVisibility(View.GONE);
                descriptionLayout.setVisibility(View.GONE);
                benefitedLayout.setVisibility(View.GONE);
                foundedInLayout.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(getActivity(), "Usuário não logado", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePhoto() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference photoRef = mStorage.child("profile_images").child(uid);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(photoRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(backdrop);
    }

    private void logout() {
        SaveData sv = new SaveData(getActivity());
        sv.logout();
        startActivity(new Intent(getActivity(), InitialActivity.class));
    }
}
