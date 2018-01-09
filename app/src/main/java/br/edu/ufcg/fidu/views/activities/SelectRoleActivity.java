package br.edu.ufcg.fidu.views.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.views.fragments.DoneeSignupFragment;
import br.edu.ufcg.fidu.views.fragments.DonorSignupFragment;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class SelectRoleActivity extends AppCompatActivity {

    public static Context context;
    private SegmentedButtonGroup mSbg;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        context = this;
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signup_content, new DonorSignupFragment());
        ft.commit();

        mSbg = findViewById(R.id.segmentedButtonGroup);
        mSbg.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {
                if(position == 0)
                    callDonorSignup();
                else if(position == 1)
                    callDoneeSignup();
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SelectRoleActivity.this, InitialActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}
