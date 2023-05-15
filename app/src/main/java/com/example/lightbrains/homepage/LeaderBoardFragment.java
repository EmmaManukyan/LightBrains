package com.example.lightbrains.homepage;

import static com.example.lightbrains.common.ConstantsForFireBase.mAuth;
import static com.example.lightbrains.common.ConstantsForFireBase.myDataBase;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lightbrains.R;
import com.example.lightbrains.adapters.RecyclerAdapterLeaderBoard;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentLeaderBoardBinding;
import com.example.lightbrains.firebase_classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderBoardFragment extends Fragment {

    private FragmentLeaderBoardBinding binding;
    private int usersCount = 10;
    private List<User> users;

    private RecyclerAdapterLeaderBoard adapter;
    private RecyclerView.LayoutManager lm;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLeaderBoardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity.binding.frContainer.setVisibility(View.VISIBLE);
        init();
        binding.progressBrLeaderBoard.setVisibility(View.VISIBLE);
        new MyAsyncTask().execute();
        users = new ArrayList<>();
    }

    private void init() {
        lm = new LinearLayoutManager(getContext());

    }

    private class MyAsyncTask extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressBrLeaderBoard.setVisibility(View.VISIBLE);
            Log.d("fir", "mtanq");
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            saveUserScores();
            return getDataFromDB();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            Log.d("fir", "its me  " + users.toString());


        }
    }

    public void saveUserScores() {
        String id = myDataBase.getKey();
        FirebaseUser curUser = mAuth.getCurrentUser();
        assert curUser != null;
//            Log.d("taguhi", "saveuser:  " + Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null));
        if (id != null) {
            myDataBase.child(curUser.getUid()).child(Constants.SCORES).setValue(Constants.sharedPreferences.getInt(Constants.SCORES, -1000));
        }
    }


    private List<User> getDataFromDB() {
        List<User> topUsers = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(ConstantsForFireBase.USER_KEY);
        Query myTopUsers = ref.orderByChild(Constants.SCORES).limitToFirst(10);
        myTopUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topUsers.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    assert user != null;
                    Log.d("fir", "" + user.getUserName());
                    if (user.isEmailIsVerified()){
                        topUsers.add(user);
                    }
                }
                binding.progressBrLeaderBoard.setVisibility(View.GONE);
                adapter = new RecyclerAdapterLeaderBoard(getContext(), topUsers);
                Log.d("fir", "hello " + topUsers.size());
                for (User topUser : topUsers) {
                    Log.d("fir", topUser.getUserName());
                }
                binding.recLeaderBoard.setLayoutManager(lm);
                binding.recLeaderBoard.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return topUsers;

    }

}