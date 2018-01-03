package br.edu.ufcg.fidu;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

public class SelectRoleActivity extends AppCompatActivity {
    private RadioButton rbtnDonator;
    private RadioButton rbtnDonatary;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        // Fragment
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signup_content, new DonaterSignupFragment());
        ft.commit();

        // UI Components
        rbtnDonator = (RadioButton) findViewById(R.id.rbtnDonater);
        rbtnDonatary = (RadioButton) findViewById(R.id.rbtnDonatary);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbtnDonater:
                if (checked)
                    callDonaterSignup();
                    break;
            case R.id.rbtnDonatary:
                if (checked)
                    callDonatarySignup();
                    break;
        }
    }

    private void callDonaterSignup() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.signup_content, new DonaterSignupFragment());
        ft.commit();
    }

    private void callDonatarySignup() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.signup_content, new DonatorySignupFragment());
        ft.commit();
    }
}
