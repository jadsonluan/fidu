package br.edu.ufcg.fidu.views.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.views.fragments.MapFragment;
import br.edu.ufcg.fidu.views.fragments.MessagesFragment;
import br.edu.ufcg.fidu.views.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private android.support.v4.app.FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

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
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        navigation.setSelectedItemId(R.id.navigation_messages);
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
                    changeFragment(MapFragment.newInstance(-23.556822, -46.729966),
                            getString(R.string.title_messages));
                    return true;
                case R.id.navigation_profile:
                    changeFragment(new ProfileFragment(), getString(R.string.title_profile));
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
}
