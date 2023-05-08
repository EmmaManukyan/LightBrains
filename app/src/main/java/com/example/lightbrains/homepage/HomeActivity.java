package com.example.lightbrains.homepage;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;

import static com.example.lightbrains.common.Constants.languageLogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.ActivityHomeBinding;
import com.example.lightbrains.firebase_classes.User;
import com.example.lightbrains.firstpages.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public static ActivityHomeBinding binding;
    private boolean IS_IN_PROFILE_PAGE = false;
    private int PAGE_COUNTER = 0;


    @Override
    protected void onStart() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int CHECKED_LANGUAGE = sh.getInt(Constants.CHECKED_LANGUAGE, 0);
        setLocal(HomeActivity.this,languageLogs[CHECKED_LANGUAGE]);

        super.onStart();
    }

    public void setLocal(Activity activity, String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setLocalNightMode(MODE_NIGHT_NO);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, new HomeFragment()).commit();
        binding.myBottomNav.setSelectedItemId(R.id.menu_home);


        binding.imgProfile.setOnClickListener(view -> {
            IS_IN_PROFILE_PAGE = true;
            Fragment fragment = new ProfileFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_anim_slide_in, R.anim.enter_anim_from_right).replace(R.id.fr_container, fragment).addToBackStack(null).commit();
            binding.myBottomNav.setSelectedItemId(R.id.menu_profile);
        });


        Constants.createSharedPreferences(HomeActivity.this);
        if (Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null) == null) {
//            Toast.makeText(this, "CHKa", Toast.LENGTH_SHORT).show();
            getDataFromDB();
        } else {
//            Toast.makeText(this, "Ka", Toast.LENGTH_SHORT).show();
            binding.tvProfileName.setText(Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null));
            Picasso.get().load(Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null)).placeholder(R.drawable.img_profile_default).into(binding.imgProfile);

        }


        binding.myBottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.menu_home:
                    if (PAGE_COUNTER != 0) {
                        binding.frContainer.setVisibility(View.GONE);
                        IS_IN_PROFILE_PAGE = false;
                        fragment = new HomeFragment();
                        PAGE_COUNTER = 0;
                    }
                    break;
                case R.id.menu_profile:
                    if (PAGE_COUNTER != 1) {
                        binding.frContainer.setVisibility(View.GONE);
                        IS_IN_PROFILE_PAGE = true;
                        fragment = new ProfileFragment();
                        PAGE_COUNTER = 1;
                    }
                    break;
                case R.id.menu_settings:
                    if (PAGE_COUNTER != 2) {
                        binding.frContainer.setVisibility(View.GONE);
                        IS_IN_PROFILE_PAGE = false;
                        fragment = new SettingsFragment();
                        PAGE_COUNTER = 2;
                    }
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

    @Override
    protected void onResume() {
        super.onResume();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, new HomeFragment()).commit();
        //binding.myBottomNav.setSelectedItemId(R.id.menu_home);
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
            binding.tvProfileName.setVisibility(View.INVISIBLE);
        }
    }

    private void getDataFromDB() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(ConstantsForFireBase.USER_KEY);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(getIntent().getStringExtra(ConstantsForFireBase.USER_KEY)).getValue(User.class);
                if (user != null) {
                    binding.tvProfileName.setText(user.getUserName());
                    Picasso.get().load(user.getImageUri()).placeholder(R.drawable.img_profile_default).into(binding.imgProfile);
                    Constants.myEditShared.putString(ConstantsForFireBase.USER_NAME, user.getUserName());
                    Constants.myEditShared.putString(ConstantsForFireBase.PROFILE_IMAGE_URI, user.getImageUri());
                    Log.d("taguhi", "" + user.getImageUri());
                    Constants.myEditShared.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}