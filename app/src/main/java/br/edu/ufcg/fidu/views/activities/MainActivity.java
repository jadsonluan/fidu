package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ufcg.fidu.R;

public class MainActivity extends AppCompatActivity {

    private Button mBtnLogin;
    private Button mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnSignUp = findViewById(R.id.btnCadastrar);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void login() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void signUp() {
        startActivity(new Intent(MainActivity.this, SelectRoleActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

}
