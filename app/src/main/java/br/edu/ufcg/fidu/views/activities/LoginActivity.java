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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.utils.FirebaseConnection;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private View loginProgress;
    private View loginForm;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginProgress = findViewById(R.id.loginProgress);
        loginForm = findViewById(R.id.loginForm);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        Button btnForgot = findViewById(R.id.btnForgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, InitialActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (!validate(email, password)) return;

        showProgress(true);
        mAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                afterSignIn(task);
            }
        });
    }

    private void afterSignIn(Task<AuthResult> task) {
        showProgress(false);

        if (task.isSuccessful()) {
            FirebaseConnection firebaseConnection = new FirebaseConnection(LoginActivity.this);

            try {
                String uid = mAuth.getCurrentUser().getUid();
                firebaseConnection.saveUser(uid);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.default_error_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            handleErrors(task.getException());
        }
    }

    private void handleErrors(Exception exception) {
        try {
            throw exception;
        } catch (FirebaseAuthInvalidUserException ex) {
            etEmail.setError(getString(R.string.inexistent_email));
            etEmail.requestFocus();
        } catch(FirebaseAuthInvalidCredentialsException e) {
            etPassword.setError(getString(R.string.wrong_password));
            etPassword.requestFocus();
        } catch (FirebaseNetworkException e) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.default_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(String email, String password) {
        boolean isValid = true;
        View focusView = null;

        if (email == null || email.trim().equals("")) {
            etEmail.setError(getResources().getText(R.string.email_empty));
            focusView = etEmail;
            isValid = false;
        } else if (!email.contains("@")) {
            etEmail.setError(getResources().getText(R.string.email_invalid));
            focusView = etEmail;
            isValid = false;
        }

        if (password == null || password.trim().equals("")) {
            etPassword.setError(getResources().getText(R.string.password_empty));
            focusView = etPassword;
            isValid = false;
        }

        if (!isValid) focusView.requestFocus();
        return isValid;
    }

    private void forgotPassword() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void showProgress(boolean show) {
        loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        loginProgress.setVisibility(show ? View.VISIBLE: View.GONE);
    }

}
