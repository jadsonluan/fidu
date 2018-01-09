package br.edu.ufcg.fidu.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.activities.InitialActivity;

/**
 * Created by vitoria on 08/01/18.
 */

public class ProfileFragment extends Fragment {
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        SaveData sv = new SaveData(getActivity());
        sv.logout();
        startActivity(new Intent(getActivity(), InitialActivity.class));
    }
}
