package com.example.lightbrains.part_second.attention_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameWriteAnswersBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.interfaces.BackPressedListener;

import java.util.HashMap;


public class AttentionGameWriteAnswersFragment extends Fragment implements BackPressedListener{
    private FragmentAttentionGameWriteAnswersBinding binding;
    private RecyclerView.LayoutManager lm;
    private AttentionGameRecyclerAdapter adapter;
    private HashMap<Integer, Integer> showedMap;
    private int figureType;

    private int figuresGroupCount;

    private boolean answersAreChecked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttentionGameWriteAnswersBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        figuresGroupCount = bundle.getInt(Constants.FIGURES_GROUP_COUNT);
        showedMap = new HashMap<>();
        HashMap<Integer, Integer> tempShowedMap = (HashMap<Integer, Integer>) bundle.getSerializable(Constants.HASHMAP_BUNDLE);
        for (Integer key : tempShowedMap.keySet()) {
            if (tempShowedMap.get(key) != 0) {
                showedMap.put(key, tempShowedMap.get(key));
            }
        }
        figureType = bundle.getInt(Constants.FIGURES_TYPE);
        Log.d("TAG", "jjjjjjjjjjjjjjjjjjjjjjj             " + showedMap.toString());
        init();

        binding.btnCheck.setOnClickListener(view1 -> {
            if (!answersAreChecked) {
                Constants.closeKeyboard(requireActivity());
                adapter.isChecking = true;
                answersAreChecked = true;
                adapter.notifyDataSetChanged();
                if (figuresGroupCount!=0) {
                    binding.btnCheck.setText(getResources().getString(R.string.next));
                }else{
                    binding.btnCheck.setText(getResources().getString(R.string.finish));
                }
            } else if (figuresGroupCount==0) {
                Bundle bundleToNavigate = new Bundle();
                bundleToNavigate.putInt(Constants.SCORES,10);
                bundleToNavigate.putInt(Constants.COUNT_FLASH_CARDS,10);
                bundleToNavigate.putLong(Constants.FIGURES_SHOW_TIME,System.currentTimeMillis()-bundle.getLong(Constants.FIGURES_SHOW_START_TIME));
                Navigation.findNavController(view).navigate(R.id.action_attentionGameWriteAnswersFragment_to_showResultsFragment3,bundleToNavigate);
            } else {
                Bundle bundleToNavigate = new Bundle();
                bundle.putInt(Constants.FIGURES_GROUP_COUNT,figuresGroupCount);
                bundle.putInt(Constants.FIGURES_TYPE,bundle.getInt(Constants.FIGURES_TYPE));
                bundle.putInt(Constants.FIGURES_LEVEL,bundle.getInt(Constants.FIGURES_LEVEL));
                bundle.putFloat(Constants.FIGURES_SHOW_TIME,bundle.getFloat(Constants.FIGURES_SHOW_TIME));
                bundle.putInt(Constants.FIGURES_COUNT,bundle.getInt(Constants.FIGURES_COUNT));
                bundleToNavigate.putLong(Constants.FIGURES_SHOW_START_TIME,bundle.getLong(Constants.FIGURES_SHOW_START_TIME));
                Navigation.findNavController(view).navigate(R.id.action_attentionGameWriteAnswersFragment_to_attentionGameShowFiguresFragment,bundleToNavigate);
            }
        });

    }

    private void init() {
        binding.myRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttentionGameRecyclerAdapter(showedMap, getContext(), figureType);
        binding.myRec.setAdapter(adapter);
    }

    public static BackPressedListener backpressedlistener;

    @Override
    public void onPause() {
        backpressedlistener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        backpressedlistener = this;
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        CustomDialogFragmentForExit customDialogFragmentForExit = new CustomDialogFragmentForExit(4);
        customDialogFragmentForExit.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG_EXIT);
    }
}