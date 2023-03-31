package com.example.lightbrains.homepage;

import static com.example.lightbrains.R.string.flash_anzan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.adapters.RecyclerAdapterFlashCards;
import com.example.lightbrains.part_first_mental.flashanzan.FLashActivity;
import com.example.lightbrains.part_first_mental.flashanzan.RecyclerViewItem;
import com.example.lightbrains.part_first_mental.mental_counting.MentalCountingActivity;
import com.example.lightbrains.part_second.attention_game.AttentionGameActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ConstraintLayout viewMental;
    private ConstraintLayout viewMemoryGames;
    private List<Activity> activities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        viewMental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet(0);
            }
        });


        viewMemoryGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openBottomSheet(1);
            }
        });
    }

    private void init(View view) {
        viewMental = view.findViewById(R.id.view_mental);
        viewMemoryGames = view.findViewById(R.id.view_memory_games);

    }

    private void openBottomSheet(int viewOrder) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.MyStyleForBottomSheetDialog1);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);

        bottomSheetDialog.show();

        List<RecyclerViewItem> list = new ArrayList<RecyclerViewItem>();
        list = getData(viewOrder);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.my_recycler_in_bottom_sheet);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        RecyclerAdapterFlashCards adapter = new RecyclerAdapterFlashCards(list, getContext(), new RecyclerAdapterFlashCards.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewItem item, int position) {
                Intent intent = null;
                if (viewOrder == 0) {
                    switch (position) {
                        case 0:
                            intent = new Intent(getActivity(), MentalCountingActivity.class);
                            break;
                        case 1:
                            intent = new Intent(getActivity(), FLashActivity.class);
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            intent = new Intent(getActivity(), AttentionGameActivity.class);
                            break;
                        case 1:
                            intent = new Intent(getActivity(), FLashActivity.class);
                            break;
                    }
                }
                bottomSheetDialog.dismiss();
                Toast.makeText(getContext(), "" + item.getTitleName(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);



    }


    private List<RecyclerViewItem> getData(int viewOrder) {
        List<RecyclerViewItem> list = new ArrayList<>();
        if (viewOrder == 0) {
            list.add(new RecyclerViewItem(R.drawable.img_for_mental, getActivity().getResources().getString(flash_anzan), R.color.pink));
            list.add(new RecyclerViewItem(R.drawable.flash_cards_img, getActivity().getResources().getString(R.string.flash_cards), R.color.blue));
        } else if (viewOrder == 1) {
            list.add(new RecyclerViewItem(R.drawable.img_for_mental, getActivity().getResources().getString(R.string.game_1), R.color.green));
            list.add(new RecyclerViewItem(R.drawable.flash_cards_img, getActivity().getResources().getString(R.string.game_2), R.color.yellow));
        }
        return list;
    }


}