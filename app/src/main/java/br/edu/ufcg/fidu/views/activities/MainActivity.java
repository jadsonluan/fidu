package br.edu.ufcg.fidu.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.fragments.HomeFragment;
import br.edu.ufcg.fidu.views.fragments.MapFragment;
import br.edu.ufcg.fidu.views.fragments.MessagesFragment;
import br.edu.ufcg.fidu.views.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private android.support.v4.app.FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ApplicationInfo app = null;

        try {
            app = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;

            if (bundle != null) {
                if (bundle.containsKey("show_map")) {
                    navigation.setSelectedItemId(R.id.navigation_map);
                } else if (bundle.containsKey("show_profile")) {
                    navigation.setSelectedItemId(R.id.navigation_profile);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_messages:
                    changeFragment(new MessagesFragment(), getString(R.string.title_messages));
                    return true;
                case R.id.navigation_map:
                    changeFragment(new MapFragment(),
                            getString(R.string.title_map));
                    return true;
                case R.id.navigation_profile:
                    changeFragment(new ProfileFragment(), getString(R.string.title_profile));
                    return true;

                case R.id.navigation_home:
                    changeFragment(new HomeFragment(), getString(R.string.title_home));
                    return true;
            }
            return false;
        }
    };

    private void changeFragment(Fragment fragment, String title){
        getSupportActionBar().setTitle(title);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.logout_title))
                .setMessage(getString(R.string.msg_confirm_logout))
                .setNegativeButton(getString(R.string.no), null)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveData sv = new SaveData(MainActivity.this);
                        sv.logout();
                        startActivity(new Intent(MainActivity.this, InitialActivity.class));
                        finish();
                    }

                })
                .show();
    }
}
