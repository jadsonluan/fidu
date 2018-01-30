package br.edu.ufcg.fidu.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.utils.SaveData;

public class HomeFragment extends Fragment {

    private TextView tvName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvGreetings);
        updateUI();
    }

    private void updateUI() {
        SaveData sv = new SaveData(getActivity());

        if (sv.isLogged()) {
            if (sv.isLogged()) {
                User user;

                switch (sv.getRole()) {
                    case SaveData.DONEE:
                        user = sv.readDonee();
                        break;
                    case SaveData.DONOR:
                        user = sv.readDonor();
                        break;
                    default:
                        return;
                }

                tvName.setText("Olá, " + user.getName() + "!");
            }
        }
    }
}
