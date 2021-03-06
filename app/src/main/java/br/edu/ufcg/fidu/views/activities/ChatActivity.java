package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
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
    private LinearLayout layout;
    private ImageView sendButton;
    private EditText messageArea;
    private ScrollView scrollView;
    private DatabaseReference reference1, reference2;

    public static final String USER_UID = "user_uid";
    public static final String OTHER_UID = "other_uid";
    public static final String OTHERNAME = "othername";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        Intent intent = getIntent();
        if(intent != null){
            final String userUID = intent.getStringExtra(USER_UID);
            final String otherUID = intent.getStringExtra(OTHER_UID);
            final String othername = intent.getStringExtra(OTHERNAME);
            setTitle(othername);

            String userPath = "chats/" + userUID + "/talking_to/" + otherUID;
            reference1 = FirebaseDatabase.getInstance().getReference(userPath);

            String otherPath = "chats/" + otherUID + "/talking_to/" + userUID;
            reference2 = FirebaseDatabase.getInstance().getReference(otherPath);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String messageText = messageArea.getText().toString();

                    if(!messageText.equals("")){
                        Map<String, String> map = new HashMap<>();
                        map.put("message", messageText);
                        map.put("user", userUID);
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

                    if (message.getUser().equals(userUID)) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}