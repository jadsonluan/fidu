package ufcg.edu.br.fidu;

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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    // UI Components
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // UI Components
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                login(email, password);
            }
        });
    }

    private void login(String email, String password) {
        if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
            Toast.makeText(LoginActivity.this, "E-mail ou senha vazios.", Toast.LENGTH_LONG)
                .show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Autenticado", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Falha ao autenticar", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
