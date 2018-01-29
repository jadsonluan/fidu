package br.edu.ufcg.fidu.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.activities.ChatActivity;

public class MessagesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_go_to_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData saveData = new SaveData(getActivity());
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                if(saveData.getRole() == 0){
                    intent.putExtra("user", "vitoria");
                    intent.putExtra("chatWith", "veronica");
                } else {
                    intent.putExtra("user", "veronica");
                    intent.putExtra("chatWith", "vitoria");
                }
                startActivity(intent);
            }
        });
    }
}
