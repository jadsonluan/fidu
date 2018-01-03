package br.edu.ufcg.fidu;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SelectRoleActivity extends AppCompatActivity {

    private RadioButton rbtnDonor;
    private RadioButton rbtnDonee;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        // Fragment
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signup_content, new DonorSignupFragment());
        ft.commit();

        // UI Components
        rbtnDonor = (RadioButton) findViewById(R.id.rbtnDonor);
        rbtnDonee = (RadioButton) findViewById(R.id.rbtnDonee);
    }

    private void callDonorSignup() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.signup_content, new DonorSignupFragment());
        ft.commit();
    }

    private void callDoneeSignup() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.signup_content, new DoneeSignupFragment());
        ft.commit();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbtnDonor:
                if (checked)
                    callDonorSignup();
                    break;
            case R.id.rbtnDonee:
                if (checked)
                    callDoneeSignup();
                    break;
        }
    }

}
