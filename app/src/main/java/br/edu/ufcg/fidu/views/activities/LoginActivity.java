package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;
import br.edu.ufcg.fidu.utils.FirebaseConnection;
import br.edu.ufcg.fidu.utils.SaveData;

public class LoginActivity extends AppCompatActivity {

    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnLogin;

    private FirebaseAuth mAuth;

    private void login(String email, String password) {
        if (email == null || email.trim().equals("")) {
            Toast.makeText(LoginActivity.this, R.string.email_empty, Toast.LENGTH_LONG)
                    .show();
        }

        else if (password == null || password.trim().equals("")) {
            Toast.makeText(LoginActivity.this, R.string.password_empty, Toast.LENGTH_LONG)
                    .show();
        }

        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseConnection firebaseConnection = new FirebaseConnection(LoginActivity.this);
                        firebaseConnection.saveUser(mAuth.getCurrentUser().getUid());
                        Toast.makeText(LoginActivity.this, R.string.auth_success, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEtEmail = findViewById(R.id.etEmail);
        mEtPassword = findViewById(R.id.etPassword);
        mBtnLogin = findViewById(R.id.btnLogin);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();

                login(email, password);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, InitialActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
