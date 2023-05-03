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
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameWriteAnswersBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.interfaces.BackPressedListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class AttentionGameWriteAnswersFragment extends Fragment implements BackPressedListener {
    private FragmentAttentionGameWriteAnswersBinding binding;
    private RecyclerView.LayoutManager lm;
    private AttentionGameRecyclerAdapter adapter;
    private HashMap<Integer, Integer> showedMap;
    private int figureType;

    private int figuresGroupCount;

    private boolean answersAreChecked = false;

    private int scores = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        assert bundle != null;
        //figuresGroupCount = bundle.getInt(Constants.FIGURES_GROUP_COUNT);
        figuresGroupCount = AttentionGameValues.getFiguresGroupCount();
        showedMap = new HashMap<>();
        HashMap<Integer, Integer> tempShowedMap = (HashMap<Integer, Integer>) bundle.getSerializable(Constants.HASHMAP_BUNDLE);
        for (Integer key : tempShowedMap.keySet()) {
            if (tempShowedMap.get(key) != 0) {
                showedMap.put(key, tempShowedMap.get(key));
            }
        }
        // figureType = bundle.getInt(Constants.FIGURES_TYPE);
        figureType = AttentionGameValues.getFiguresType();
        init();

        binding.btnCheck.setOnClickListener(view1 -> {
            if (!answersAreChecked) {
                Constants.closeKeyboard(requireActivity());
                adapter.isChecking = true;
                answersAreChecked = true;
                if (!adapter.getAnswersMap().containsValue(false) && adapter.getAnswersMap().containsValue(true)) {
                    scores += 5;
                    Toast.makeText(getContext(), "Excellent", Toast.LENGTH_SHORT).show();
                } else {
                    for (Integer key : adapter.getAnswersMap().keySet()) {
                        scores = Boolean.TRUE.equals(adapter.getAnswersMap().get(key)) ? scores + 1 : scores - 1;
                    }
                }
                adapter.notifyDataSetChanged();
                if (figuresGroupCount != 0) {
                    binding.btnCheck.setText(getResources().getString(R.string.next));
                } else {
                    binding.btnCheck.setText(getResources().getString(R.string.finish));
                }
            } else if (figuresGroupCount == 0) {
                Bundle bundleToNavigate = new Bundle();
                bundleToNavigate.putInt(Constants.RIGHT_ANSWERS, scores);
                bundleToNavigate.putInt(Constants.COUNT_FLASH_CARDS, Constants.sharedPreferences.getInt(Constants.FIGURES_GROUP_COUNT, 0));
                bundleToNavigate.putLong(Constants.FIGURES_SHOW_TIME, System.currentTimeMillis() - bundle.getLong(Constants.FIGURES_SHOW_START_TIME));
                Navigation.findNavController(view).navigate(R.id.action_attentionGameWriteAnswersFragment_to_showResultsFragment3, bundleToNavigate);
            } else {

                Bundle bundleToNavigate = new Bundle();
                //bundleToNavigate.putInt(Constants.FIGURES_GROUP_COUNT,figuresGroupCount);
                //bundleToNavigate.putInt(Constants.FIGURES_TYPE,bundle.getInt(Constants.FIGURES_TYPE));
                //bundleToNavigate.putInt(Constants.FIGURES_LEVEL,bundle.getInt(Constants.FIGURES_LEVEL));
                //bundleToNavigate.putFloat(Constants.FIGURES_SHOW_TIME,bundle.getFloat(Constants.FIGURES_SHOW_TIME));
                //bundleToNavigate.putInt(Constants.FIGURES_COUNT,bundle.getInt(Constants.FIGURES_COUNT));
                //bundleToNavigate.putInt(Constants.FIGURES_COMPLEXITY_LEVEL,bundle.getInt(Constants.FIGURES_COMPLEXITY_LEVEL));
                //bundleToNavigate.putLong(Constants.FIGURES_SHOW_START_TIME,bundle.getLong(Constants.FIGURES_SHOW_START_TIME));
                //   Log.d("taguhi",figuresGroupCount+"  \nftype  " +bundle.getInt(Constants.FIGURES_TYPE)+"\nflvrl  "+bundle.getInt(Constants.FIGURES_LEVEL)+"\nTIME======  "+bundle.getFloat(Constants.FIGURES_SHOW_TIME)+"\nfcount  "+bundle.getInt(Constants.FIGURES_COUNT));

                Navigation.findNavController(view).navigate(R.id.action_attentionGameWriteAnswersFragment_to_attentionGameShowFiguresFragment, bundleToNavigate);
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