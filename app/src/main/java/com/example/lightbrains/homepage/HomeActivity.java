package com.example.lightbrains.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.lightbrains.R;
import com.example.lightbrains.databinding.ActivityHomeBinding;
import com.example.lightbrains.homepage.HomeFragment;
import com.example.lightbrains.homepage.LeaderBoardFragment;
import com.example.lightbrains.homepage.ProfileFragment;
import com.example.lightbrains.homepage.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;

    private boolean IS_IN_PROFILE_PAGE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, new HomeFragment()).commit();
        binding.myBottomNav.setSelectedItemId(R.id.menu_home);

        binding.myBottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        IS_IN_PROFILE_PAGE = false;
                        fragment = new HomeFragment();
                        break;
                    case R.id.menu_profile:
                        IS_IN_PROFILE_PAGE = true;
                        fragment = new ProfileFragment();
                        break;
                    case R.id.menu_settings:
                        IS_IN_PROFILE_PAGE = false;
                        fragment = new SettingsFragment();
                        break;
                    /*case R.id.leader_board:
                        fragment = new LeaderBoardFragment();
                        IS_IN_PROFILE_PAGE = false;
                        break;*/
                }
                if (fragment != null) {
                    loadFragment(fragment);
                    setPageParams();
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, fragment).commit();
    }




    private void setPageParams() {
        if (!IS_IN_PROFILE_PAGE) {
            binding.imgProfile.setVisibility(View.VISIBLE);
            binding.tvProfileName.setVisibility(View.VISIBLE);
        }else{
            binding.imgProfile.setVisibility(View.GONE);
            binding.tvProfileName.setVisibility(View.GONE);
        }

    }
}