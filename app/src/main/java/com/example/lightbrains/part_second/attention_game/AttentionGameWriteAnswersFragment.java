package com.example.lightbrains.part_second.attention_game;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameWriteAnswersBinding;

import java.util.HashMap;


public class AttentionGameWriteAnswersFragment extends Fragment {
    private FragmentAttentionGameWriteAnswersBinding binding;
    private RecyclerView.LayoutManager lm;
    private AttentionGameRecyclerAdapter adapter;
    private HashMap<Integer, Integer> showedMap;
    private int figureType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttentionGameWriteAnswersBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        showedMap= new HashMap<>();
        HashMap<Integer,Integer> tempShowedMap = (HashMap<Integer, Integer>) bundle.getSerializable(Constants.HASHMAP_BUNDLE);
        for (Integer key : tempShowedMap.keySet()) {
            if (tempShowedMap.get(key)!=0){
                showedMap.put(key,tempShowedMap.get(key));
            }
        }
        figureType = bundle.getInt(Constants.FIGURES_TYPE);
        Log.d("TAG", "jjjjjjjjjjjjjjjjjjjjjjj             " + showedMap.toString());
        init();

        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter.getHolder().getEdtAnswer().setText("1255");
                adapter.isChecking = true;
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void init() {
        binding.myRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttentionGameRecyclerAdapter(showedMap, getContext(), figureType);
        binding.myRec.setAdapter(adapter);
    }
}