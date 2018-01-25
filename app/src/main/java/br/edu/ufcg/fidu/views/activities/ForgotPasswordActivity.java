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
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.edu.ufcg.fidu.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);

        Button btnForgot = findViewById(R.id.btnForgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResetPassword();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void sendResetPassword() {
        String email = etEmail.getText().toString();

        if (email.trim().equals("")) {
            etEmail.setError(getResources().getText(R.string.email_empty));
            return;
        } else if (!email.contains("@")) {
            etEmail.setError(getResources().getText(R.string.email_invalid));
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this,
                            R.string.password_reset_sent, Toast.LENGTH_SHORT).show();
                } else {
                    handleErrors(task.getException());
                }
            }
        });
    }

    private void handleErrors(Exception exception) {
        try {
            throw exception;
        } catch (FirebaseAuthInvalidUserException e) {
            Toast.makeText(this, R.string.inexistent_email, Toast.LENGTH_SHORT).show();
        } catch (FirebaseNetworkException e) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.default_error_message, Toast.LENGTH_SHORT).show();
        }
    }
}
