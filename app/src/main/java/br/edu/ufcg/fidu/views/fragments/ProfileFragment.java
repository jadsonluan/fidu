package br.edu.ufcg.fidu.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.activities.InitialActivity;

/**
 * Created by vitoria on 08/01/18.
 */

public class ProfileFragment extends Fragment {
    private Button btnLogout;
    private TextView tvName;
    private TextView tvAddress;
    private ImageView imgProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvName);
        tvAddress = view.findViewById(R.id.tvAddress);
        imgProfile = view.findViewById(R.id.imgProfile);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        updateUI();
    }

    private void updateUI() {
        SaveData sv = new SaveData(getActivity());

        if (sv.isLogged()) {
            User user;
            int role = sv.getRole();

            if (role == SaveData.DONEE) {
                user = sv.readDonee();
            } else if (role == SaveData.DONOR) {
                user = sv.readDonor();
            } else {
                return;
            }

            tvName.setText(user.getName());

            if (user instanceof Donee) {
                tvAddress.setText(((Donee) user).getAddress());
            } else {
                tvAddress.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(getActivity(), "Usuário não logado", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        SaveData sv = new SaveData(getActivity());
        sv.logout();
        startActivity(new Intent(getActivity(), InitialActivity.class));
    }
}
