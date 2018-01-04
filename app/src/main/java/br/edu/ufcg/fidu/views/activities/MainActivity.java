package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ufcg.fidu.R;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private Button btnLogin;
    private Button btnSignUp;

    private void login() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void signUp() {
        startActivity(new Intent(MainActivity.this, SelectRoleActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Components
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnCadastrar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

}
