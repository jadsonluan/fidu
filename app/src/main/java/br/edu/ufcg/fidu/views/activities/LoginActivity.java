package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.utils.FirebaseConnection;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private Button btnForgot;
    private View loginProgress;
    private View loginForm;

    private FirebaseAuth mAuth;

    private void login(String email, String password) {
        if (!validate(email, password)) return;

        showProgress(true);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showProgress(false);
                if (task.isSuccessful()) {
                    FirebaseConnection firebaseConnection = new FirebaseConnection(LoginActivity.this);
                    firebaseConnection.saveUser(mAuth.getCurrentUser().getUid());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    handleErrors(task.getException());
                }
            }
        });
    }

    private void handleErrors(Exception exception) {
        try {
            throw exception;
        } catch (FirebaseAuthInvalidUserException ex) {
            mEtEmail.setError(getString(R.string.inexistent_email));
            mEtEmail.requestFocus();
        } catch(FirebaseAuthInvalidCredentialsException e) {
            mEtPassword.setError(getString(R.string.wrong_password));
            mEtPassword.requestFocus();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "[DEBUG] Error", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(String email, String password) {
        boolean isValid = true;
        View focusView = null;

        if (email == null || email.trim().equals("")) {
            mEtEmail.setError(getResources().getText(R.string.email_empty));
            focusView = mEtEmail;
            isValid = false;
        } else if (!email.contains("@")) {
            mEtEmail.setError(getResources().getText(R.string.email_invalid));
            focusView = mEtEmail;
            isValid = false;
        }

        if (password == null || password.trim().equals("")) {
            mEtPassword.setError(getResources().getText(R.string.password_empty));
            focusView = mEtPassword;
            isValid = false;
        }

        if (!isValid) focusView.requestFocus();
        return isValid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginProgress = findViewById(R.id.loginProgress);
        loginForm = findViewById(R.id.loginForm);

        mEtEmail = findViewById(R.id.etEmail);
        mEtPassword = findViewById(R.id.etPassword);
        mBtnLogin = findViewById(R.id.btnLogin);
        btnForgot = (Button) findViewById(R.id.btnForgot);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();

                login(email, password);
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, InitialActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
