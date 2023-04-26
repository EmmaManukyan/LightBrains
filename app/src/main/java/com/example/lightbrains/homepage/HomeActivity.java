package com.example.lightbrains.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.ActivityHomeBinding;
import com.example.lightbrains.firebase_classes.User;
import com.example.lightbrains.homepage.HomeFragment;
import com.example.lightbrains.homepage.LeaderBoardFragment;
import com.example.lightbrains.homepage.ProfileFragment;
import com.example.lightbrains.homepage.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private boolean IS_IN_PROFILE_PAGE = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, new HomeFragment()).commit();
        binding.myBottomNav.setSelectedItemId(R.id.menu_home);


        binding.imgProfile.setOnClickListener(view -> {
            IS_IN_PROFILE_PAGE = true;
            Fragment fragment = new ProfileFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_anim_slide_in,R.anim.enter_anim_from_right).replace(R.id.fr_container, fragment).addToBackStack(null).commit();
            binding.myBottomNav.setSelectedItemId(R.id.menu_profile);
        });


        Constants.createSharedPreferences(HomeActivity.this);
        if (Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null) == null) {
            Toast.makeText(this, "CHKa", Toast.LENGTH_SHORT).show();
            getDataFromDB();
        } else {
            Toast.makeText(this, "Ka", Toast.LENGTH_SHORT).show();
            binding.tvProfileName.setText(Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null));
        }




        binding.myBottomNav.setOnItemSelectedListener(item -> {
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
        } else {
            binding.imgProfile.setVisibility(View.GONE);
            binding.tvProfileName.setVisibility(View.GONE);
        }

    }

    private void getDataFromDB() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(ConstantsForFireBase.USER_KEY);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(getIntent().getStringExtra(ConstantsForFireBase.USER_KEY)).getValue(User.class);
                binding.tvProfileName.setText(user.getUserName());
                Constants.myEditShared.putString(ConstantsForFireBase.USER_NAME, user.getUserName());
                Constants.myEditShared.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}