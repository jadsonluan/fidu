package br.edu.ufcg.fidu.views.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
        changeFragment(new MessagesFragment());
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_messages:
                    changeFragment(new MessagesFragment());
                    return true;
                case R.id.navigation_map:
                    changeFragment(MapFragment.newInstance(-7.2179305, -35.906639));
                    return true;
                case R.id.navigation_profile:
                    changeFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };

    private void changeFragment(Fragment fragment){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }
}
