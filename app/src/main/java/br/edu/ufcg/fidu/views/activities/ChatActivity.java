package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Message;

public class ChatActivity extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        Intent intent = getIntent();
        if(intent != null){
            final String username = intent.getStringExtra("user");
            final String chatWith = intent.getStringExtra("chatWith");
            setTitle(chatWith);
            reference1 = FirebaseDatabase.getInstance().getReference("messages/" + username + "_" + chatWith);
            reference2 = FirebaseDatabase.getInstance().getReference("messages/" + chatWith + "_" + username);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String messageText = messageArea.getText().toString();

                    if(!messageText.equals("")){
                        Map<String, String> map = new HashMap<>();
                        map.put("message", messageText);
                        map.put("user", username);
                        map.put("date", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                        reference1.push().setValue(map);
                        reference2.push().setValue(map);
                        messageArea.setText("");
                    }
                }
            });

            reference1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);

                    if (message.getUser().equals(username)) {
                        addMessageBox(message.getMessage(), message.getDate(), 0);
                    } else {
                        addMessageBox(message.getMessage(), message.getDate(), 1);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void addMessageBox(String message, String date, int type){
        LinearLayout linearLayout = new LinearLayout(this);
        TextView tvMessage = new TextView(this);
        TextView tvDate = new TextView(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        lp.setMargins(22, 22, 22, 0);
        linearLayout.setLayoutParams(lp);
        tvDate.setLayoutParams(new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvMessage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvDate.setText(date);
        lp2.gravity = Gravity.RIGHT;
        tvDate.setLayoutParams(lp2);
        tvMessage.setText(message);

        tvDate.setText(date);
        tvMessage.setText(message);

        if(type == 1) {
            lp.gravity = Gravity.LEFT;
            linearLayout.setBackgroundResource(R.drawable.background_message_sent);
        }
        else{
            lp.gravity = Gravity.RIGHT;
            linearLayout.setBackgroundResource(R.drawable.background_message);
            tvMessage.setTextColor(getResources().getColor(android.R.color.white));
            tvDate.setTextColor(getResources().getColor(android.R.color.white));
        }
        linearLayout.addView(tvMessage);
        linearLayout.addView(tvDate);
        layout.addView(linearLayout);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}