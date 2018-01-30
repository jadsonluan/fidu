package br.edu.ufcg.fidu.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.utils.FirebaseConnection;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.activities.InitialActivity;
import br.edu.ufcg.fidu.views.activities.SearchDoneeActivity;
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
    private ProgressBar loading;

    private FloatingActionButton btnEdit;
    private Button btnLogout;
    private Button btnSearch;

    private ViewGroup occupationLayout;
    private ViewGroup descriptionLayout;
    private ViewGroup foundedInLayout;
    private ViewGroup benefitedLayout;
    private ViewGroup addressLayout;
    private ViewGroup websiteLayout;

    private SaveData sv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loading = view.findViewById(R.id.main_loading);
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

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDonee();
            }
        });

        sv = new SaveData(getActivity());
        updateUI();
    }

    private void searchDonee() {
        startActivity(new Intent(getActivity(), SearchDoneeActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateProfile() {
        startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void updateUI() {
        if (sv.isLogged()) {
            User user;

            switch(sv.getRole()) {
                case SaveData.DONEE: user = sv.readDonee(); break;
                case SaveData.DONOR: user = sv.readDonor(); break;
                default: return;
            }

            tvName.setText(user.getName());
            setInformation(user.getOccupation(), tvOccupation, occupationLayout);
            setInformation(user.getWebsite(), tvWebsite, websiteLayout);
            loadPhoto(user.getPhotoUrl());

            if (user instanceof Donee) {
                Donee donee = (Donee) user;
                String benefited, foundedIn;

                benefited = donee.getBenefited() == 0 ? "" : donee.getBenefited() + " pessoa(s)";
                foundedIn = donee.getFoundedIn() == 0 ? "" : donee.getFoundedIn() + "";

                setInformation(donee.getAddress(), tvAddress, addressLayout);
                setInformation(donee.getDescription(), tvDescription, descriptionLayout);
                setInformation(benefited, tvBenefited, benefitedLayout);
                setInformation(foundedIn, tvFoundedIn, foundedInLayout);
            } else {
                addressLayout.setVisibility(View.GONE);
                descriptionLayout.setVisibility(View.GONE);
                benefitedLayout.setVisibility(View.GONE);
                foundedInLayout.setVisibility(View.GONE);
            }
        } else {
            // Por um loading
            Toast.makeText(getActivity(), "Usuário não logado", Toast.LENGTH_SHORT).show();
        }
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

    private void logout() {
        SaveData sv = new SaveData(getActivity());
        sv.logout();
        startActivity(new Intent(getActivity(), InitialActivity.class));
        getActivity().finish();
    }
}
