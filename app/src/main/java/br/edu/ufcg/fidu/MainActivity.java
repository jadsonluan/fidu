package br.edu.ufcg.fidu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private Button btnEntrar;
    private Button btnCadastrar;

    private void entra() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void cadastra() {
        startActivity(new Intent(MainActivity.this, SelectRoleActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Components
        btnEntrar = (Button) findViewById(R.id.btnLogin);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entra();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastra();
            }
        });
    }

}
