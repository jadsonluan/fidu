package br.edu.ufcg.fidu.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.utils.ChatAdapter;
import br.edu.ufcg.fidu.utils.SaveData;

public class MessagesFragment extends Fragment {

    private DatabaseReference mDatabase;
    private User user;
    private ListView messageList;
    private ChatAdapter adapter;

    private TextView loadingText;
    private ProgressBar loadingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadingBar = view.findViewById(R.id.loadingBar);
        loadingText = view.findViewById(R.id.loadingText);
        messageList = view.findViewById(R.id.listMessages);

        SaveData sv = new SaveData(getActivity());
        user = sv.getUser();

        populateListView();
    }

    private void populateListView() {
        showProgress(true);
        Query query = mDatabase.child("chats").child(user.getUid()).child("talking_to");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                ArrayList<String> otherUIDs = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String otherId = data.getKey();
                    otherUIDs.add(otherId);
                }

                fillAdapter(otherUIDs);
                showProgress(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgress(boolean show) {
        loadingBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loadingText.setVisibility(show ? View.VISIBLE : View.GONE);
        messageList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void fillAdapter(ArrayList<String> otherUIDs) {
        Log.i("MessagesFragment", "fillAdapter()->countIDs" + otherUIDs.size());
        boolean isDonee = user instanceof Donee;
        adapter = new ChatAdapter(user.getUid(), isDonee, otherUIDs, getActivity());

        messageList.onRemoteAdapterConnected();
        messageList.setAdapter(adapter);
        Log.i("MessagesFragment", "afterFillAdapter()");
    }
}
